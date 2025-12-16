package com.joseleandro.taskclass.common.composables

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class CutTopEndRoundedShape(
    private val roundedRadius: CornerSize = CornerSize(16.dp),
    private val cutSize: CornerSize = CornerSize(30.dp)
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val rr = roundedRadius.toPx(size, density)   // rounded radius
        val cut = cutSize.toPx(size, density)        // cut corner size

        val w = size.width
        val h = size.height

        val path = Path().apply {

            // Start bottom-left (rounded)
            moveTo(0f, h - rr)
            quadraticTo(0f, h, rr, h)

            // Bottom-right (rounded)
            lineTo(w - rr, h)
            quadraticTo(w, h, w, h - rr)

            // RIGHT side up to top-end cut
            lineTo(w, cut)

            // Cut top-end
            lineTo(w - cut, 0f)

            // Top-left (rounded)
            lineTo(rr, 0f)
            quadraticTo(0f, 0f, 0f, rr)

            close()
        }

        return Outline.Generic(path)
    }
}
