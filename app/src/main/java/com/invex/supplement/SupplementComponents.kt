package com.invex.supplement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.invex.R
import com.invex.ui.theme.CardLabelColor
import com.invex.ui.theme.HoverSelect
import com.invex.ui.theme.IconColor
import com.invex.ui.theme.InvexType
import com.invex.ui.theme.Orange
import com.invex.ui.theme.OutlineShadow
import com.invex.ui.theme.PillGrey
import com.invex.ui.theme.SoftWhite
import com.invex.ui.theme.StatusGreen
import com.invex.ui.theme.StatusRed
import com.invex.ui.theme.StatusTeal
import com.invex.ui.theme.StatusYellow
import com.invex.ui.theme.TableRowInteraction

private val Hairline = BorderStroke(1.dp, OutlineShadow)

/** Chevron + orange sheet icon + underlined "Supplements" heading. */
@Composable
fun SupplementsHeader(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack)
                .testTag("back_button"),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_chevron_back),
                contentDescription = "Back",
                modifier = Modifier.size(30.dp),
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Orange),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_supplement_sheet),
                    contentDescription = "Supplement Sheet Icon",
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = "Supplements",
                style = InvexType.PageHeading,
                color = IconColor,
                textDecoration = TextDecoration.Underline,
            )
        }
    }
}

/** One of the four count cards: status dot, label, big number. */
@Composable
fun StatusCard(
    label: String,
    count: Int,
    dotColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else modifier

    Box(
        modifier = clickableModifier
            .height(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(HoverSelect)
            .border(Hairline, RoundedCornerShape(10.dp)),
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 10.dp)
                .size(10.dp)
                .background(dotColor, RoundedCornerShape(4.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp)),
        )
        Text(
            text = label,
            style = InvexType.CardLabel,
            color = CardLabelColor,
            modifier = Modifier.padding(start = 30.dp, top = 8.dp),
        )
        Text(
            text = "%02d".format(count),
            style = InvexType.CardCount,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp, top = 30.dp),
        )
    }
}

/** 2x2 grid of status cards. */
@Composable
fun StatusGrid(
    summary: SheetSummary,
    modifier: Modifier = Modifier,
    onStockClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            StatusCard(
                label = "Sheet Issued",
                count = summary.issued,
                dotColor = StatusGreen,
                modifier = Modifier.weight(1f).testTag("card_sheet_issued"),
            )
            StatusCard(
                label = "Awaiting Return",
                count = summary.awaitingReturn,
                dotColor = StatusYellow,
                modifier = Modifier.weight(1f).testTag("card_awaiting_return"),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            StatusCard(
                label = "Returned",
                count = summary.returned,
                dotColor = StatusRed,
                modifier = Modifier.weight(1f).testTag("card_returned"),
            )
            StatusCard(
                label = "Sheets in stock",
                count = summary.inStock,
                dotColor = StatusTeal,
                modifier = Modifier.weight(1f).testTag("card_sheets_in_stock"),
                onClick = onStockClick,
            )
        }
    }
}

/** Big orange primary action. */
@Composable
fun IssueSheetButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(15.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(shape)
            .background(Orange)
            .border(Hairline, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .testTag("issue_sheet_button"),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_circle_plus),
            contentDescription = "Issue Pass",
            modifier = Modifier.size(50.dp),
        )
        Text(
            text = "Issue a Supplementary Sheet",
            style = InvexType.ButtonLabel,
            color = SoftWhite,
        )
    }
}

/** Search field built from BasicTextField so Material's default TextField padding/height can't interfere. */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(15.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(shape)
            .background(TableRowInteraction)
            .border(Hairline, shape)
            .padding(horizontal = 12.dp)
            .testTag("search_inbox_container"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = "Search",
            modifier = Modifier.size(32.dp),
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (query.isEmpty()) {
                Text(
                    text = "Search for the students",
                    style = InvexType.Body16,
                    color = Color.Black.copy(alpha = 0.6f),
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = InvexType.Body16.copy(color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_students_input"),
            )
        }
        if (query.isNotEmpty()) {
            IconButton(
                onClick = { onQueryChange("") },
                modifier = Modifier.size(32.dp).testTag("clear_search_button"),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear search",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

/** One student row: name + meta on the left, count + status pill on the right. */
@Composable
fun StudentRow(
    student: StudentSheet,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(15.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(shape)
            .background(TableRowInteraction)
            .border(Hairline, shape)
            .clickable(onClick = onClick)
            .testTag("student_row_${student.id}"),
    ) {
        Text(
            text = student.name,
            style = InvexType.Body16,
            color = Color.Black,
            modifier = Modifier.padding(start = 18.dp, top = 9.dp),
        )
        Text(
            text = student.meta,
            style = InvexType.RowMeta,
            color = Color.Black.copy(alpha = 0.75f),
            modifier = Modifier.padding(start = 18.dp, top = 36.dp),
        )
        Text(
            text = "%02d".format(student.sheetCount),
            style = InvexType.RowCount,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 15.dp, top = 7.dp),
        )
        val pillBg = when (student.status) {
            SheetStatus.RESOLVED -> PillGrey
            SheetStatus.PENDING -> StatusYellow.copy(alpha = 0.65f)
            SheetStatus.AWAITING -> StatusTeal.copy(alpha = 0.65f)
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 8.dp, top = 33.dp)
                .size(width = 60.dp, height = 15.dp)
                .background(pillBg, RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = student.status.label,
                style = InvexType.Pill,
                color = Color.Black,
            )
        }
    }
}
