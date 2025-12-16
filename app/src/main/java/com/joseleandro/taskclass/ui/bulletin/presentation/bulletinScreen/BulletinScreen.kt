package com.joseleandro.taskclass.ui.bulletin.presentation.bulletinScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.joseleandro.taskclass.ui.theme.White


data class DisciplineReport(
    val name: String,
    val teacher: String? = null,
    val grades: List<Double>,
    val maxGrade: Double = 10.0
) {
    val average: Double get() = if (grades.isEmpty()) 0.0 else grades.average()

    val status: ReportStatus
        get() = when {
            average >= 7 -> ReportStatus.APPROVED
            average >= 5 -> ReportStatus.RECOVERY
            else -> ReportStatus.FAILED
        }
}

enum class ReportStatus { APPROVED, RECOVERY, FAILED }


@Composable
fun BulletinScreen(
    modifier: Modifier = Modifier,
    viewModel: BulletinViewModel,
    onBack: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BulletinScreen(
        modifier = modifier,
        uiState = uiState,
        changeShowScore = viewModel::changeShowScore,
        onBack = onBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BulletinScreen(
    modifier: Modifier = Modifier,
    uiState: BulletinUiState,
    changeShowScore: (() -> Unit)? = null,
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                    ) {
                        Text(
                            text = "Minhas notas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Desempenho geral",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = White.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            changeShowScore?.invoke()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = White
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            text = if (uiState.showScores) "Ocultar" else "Exibir",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = if (uiState.showScores) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
            ) {
                items(uiState.scores, key = { it.name }) { discipline ->
                    DisciplineReportCardModern(
                        discipline,
                        showScore = uiState.showScores
                    )
                }
            }
        }
    }
}


@Composable
fun DisciplineReportCardModern(
    data: DisciplineReport,
    showScore: Boolean,
    modifier: Modifier = Modifier
) {

    val (statusText, statusColor) = when (data.status) {
        ReportStatus.APPROVED -> "Aprovado" to MaterialTheme.colorScheme.primary
        ReportStatus.RECOVERY -> "Recuperação" to MaterialTheme.colorScheme.tertiary
        ReportStatus.FAILED -> "Reprovado" to MaterialTheme.colorScheme.error
    }

    val targetProgress = (data.average / data.maxGrade).toFloat()
    var progress by remember { mutableFloatStateOf(0f) }
    var animatedNumberStart by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        progress = targetProgress
        animatedNumberStart = data.average.toFloat()
    }


    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "progressAnimation"
    )

    val animatedNumber by animateFloatAsState(
        targetValue = animatedNumberStart,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "numberAnimation"
    )


    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = .4.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Surface(modifier = Modifier.padding(20.dp)) {

            Column() {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = data.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        data.teacher?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    StatusBadge(text = statusText, color = statusColor)
                }

                Spacer(Modifier.height(16.dp))

                AnimatedVisibility(visible = showScore) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        data.grades.forEachIndexed { index, grade ->
                            NoteChipModern("N${index + 1}", grade)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Média final",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    color = statusColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )

                Spacer(Modifier.height(6.dp))


                Text(
                    text = String.format("%.1f", animatedNumber),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = statusColor
                )
            }
        }
    }
}


@Composable
fun NoteChipModern(label: String, grade: Double) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = String.format("%.1f", grade),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun StatusBadge(text: String, color: androidx.compose.ui.graphics.Color) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun BulletinScreenPreview() {
    TaskClassTheme(dynamicColor = false, darkTheme = false) {
        BulletinScreen(
            onBack = {},
            uiState = BulletinUiState()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BulletinScreenDarkPreview() {
    TaskClassTheme(dynamicColor = false, darkTheme = true) {
        BulletinScreen(
            onBack = {},
            uiState = BulletinUiState()
        )
    }
}
