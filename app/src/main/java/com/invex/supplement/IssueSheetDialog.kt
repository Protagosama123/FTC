package com.invex.supplement

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.invex.ui.theme.IconColor
import com.invex.ui.theme.Inter
import com.invex.ui.theme.Orange
import com.invex.ui.theme.OutlineShadow
import com.invex.ui.theme.SoftWhite
import com.invex.ui.theme.TableRowInteraction

@Composable
fun IssueSheetDialog(
    stockCount: Int,
    onDismiss: () -> Unit,
    onConfirm: (name: String, course: String, year: String, roll: String, count: Int) -> Unit,
) {
    var studentName by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf("B.tech") }
    var selectedYear by remember { mutableStateOf("3rd Year") }
    var sheetCount by remember { mutableIntStateOf(1) }

    val courses = listOf("B.tech", "BCA", "MCA", "B.Sc")
    val years = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("issue_sheet_dialog"),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    text = "Issue Supplementary Sheet",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = IconColor,
                )

                Text(
                    text = "Available in stock: $stockCount sheets",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = if (stockCount > 5) Color(0xFF2E7D32) else Orange,
                )

                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Student Name") },
                    placeholder = { Text("e.g. Rahul Sharma") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        focusedLabelColor = Orange,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_student_name"),
                )

                OutlinedTextField(
                    value = rollNumber,
                    onValueChange = { rollNumber = it },
                    label = { Text("Roll Number") },
                    placeholder = { Text("e.g. 7048") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        focusedLabelColor = Orange,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_roll_number"),
                )

                // Course selector
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Course",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        courses.forEach { course ->
                            val isSelected = selectedCourse == course
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Orange else TableRowInteraction)
                                    .border(
                                        1.dp,
                                        if (isSelected) Orange else OutlineShadow,
                                        RoundedCornerShape(8.dp),
                                    )
                                    .clickable { selectedCourse = course }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                            ) {
                                Text(
                                    text = course,
                                    fontFamily = Inter,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) SoftWhite else Color.Black,
                                )
                            }
                        }
                    }
                }

                // Year selector
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Year",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        years.forEach { year ->
                            val isSelected = selectedYear == year
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Orange else TableRowInteraction)
                                    .border(
                                        1.dp,
                                        if (isSelected) Orange else OutlineShadow,
                                        RoundedCornerShape(8.dp),
                                    )
                                    .clickable { selectedYear = year }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                            ) {
                                Text(
                                    text = year,
                                    fontFamily = Inter,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) SoftWhite else Color.Black,
                                )
                            }
                        }
                    }
                }

                // Sheet count stepper
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Number of Sheets:",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        IconButton(
                            onClick = { if (sheetCount > 1) sheetCount-- },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(TableRowInteraction)
                                .border(1.dp, OutlineShadow, CircleShape),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                        Text(
                            text = "%02d".format(sheetCount),
                            fontFamily = Inter,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 4.dp),
                        )
                        IconButton(
                            onClick = { if (sheetCount < stockCount) sheetCount++ },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(TableRowInteraction)
                                .border(1.dp, OutlineShadow, CircleShape),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.Gray, fontFamily = Inter)
                    }
                    Button(
                        onClick = {
                            onConfirm(studentName, selectedCourse, selectedYear, rollNumber, sheetCount)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Orange),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("confirm_issue_button"),
                    ) {
                        Text("Confirm & Issue", color = SoftWhite, fontFamily = Inter)
                    }
                }
            }
        }
    }
}
