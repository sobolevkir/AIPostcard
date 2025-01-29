package com.sobolevkir.aipostcard.presentation.screen.album

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints

fun Modifier.rotateLayout(rotation: Rotation): Modifier {
    return when (rotation) {
        Rotation.ROT_0, Rotation.ROT_180 -> this
        Rotation.ROT_90, Rotation.ROT_270 -> then(HorizontalLayoutModifier)
    } then rotate(rotation.degrees)
}


enum class Rotation(val degrees: Float) {
    ROT_0(0f),
    ROT_90(90f),
    ROT_180(180f),
    ROT_270(270f),
}


/** Swap horizontal and vertical constraints */
private fun Constraints.transpose(): Constraints {
    return copy(
        minWidth = minHeight,
        maxWidth = maxHeight,
        minHeight = minWidth,
        maxHeight = maxWidth
    )
}

private object HorizontalLayoutModifier : LayoutModifier {
    override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
        val placeable = measurable.measure(constraints.transpose())
        return layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(measurable: IntrinsicMeasurable, width: Int): Int {
        return measurable.maxIntrinsicWidth(width)
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(measurable: IntrinsicMeasurable, width: Int): Int {
        return measurable.maxIntrinsicWidth(width)
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(measurable: IntrinsicMeasurable, height: Int): Int {
        return measurable.minIntrinsicHeight(height)
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(measurable: IntrinsicMeasurable, height: Int): Int {
        return measurable.maxIntrinsicHeight(height)
    }
}