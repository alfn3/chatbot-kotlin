package com.example.chatpos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val expenseCategories = listOf(
    "Belanja Stok",
    "Transportasi",
    "Listrik/Internet",
    "Biaya Operasional",
    "Lainnya"
)

@Composable
fun CategoryChips(
    selectedCategory: String = "",
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    expenseCategories.forEach { category ->
        val isSelected = category == selectedCategory
        if (isSelected) {
            Button(
                onClick = { onCategorySelected(category) },
                modifier = Modifier.height(32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight)
            ) {
                Text(
                    text = category,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        } else {
            OutlinedButton(
                onClick = { onCategorySelected(category) },
                modifier = Modifier.height(32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF334155)
                )
            ) {
                Text(
                    text = category,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
