package com.tools.gamebooster.views.chat_screen.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

fun Modifier.drawVerticalScrollbar(
    state: LazyListState,
    width: Float = 8f,
    color: Color = Color.LightGray
): Modifier = drawWithContent {
    drawContent()

    val firstVisibleElementIndex = state.firstVisibleItemIndex
    val needDrawScrollbar = state.isScrollInProgress || firstVisibleElementIndex > 0

    if (needDrawScrollbar) {
        val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
        val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
        val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

        drawRect(
            color = color,
            topLeft = Offset(this.size.width - width, scrollbarOffsetY),
            size = Size(width, scrollbarHeight)
        )
    }
}