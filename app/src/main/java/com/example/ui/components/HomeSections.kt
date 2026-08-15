package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FriendItem
import com.example.data.model.SubjectCategory
import com.example.ui.theme.*

@Composable
fun FriendsRow(
    friends: List<FriendItem>,
    onFriendClick: (FriendItem) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Friends",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "See All",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
                modifier = Modifier
                    .clickable { onSeeAllClick() }
                    .padding(4.dp)
                    .testTag("friends_see_all_button")
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(friends, key = { it.id }) { friend ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onFriendClick(friend) }
                        .testTag("friend_avatar_${friend.id}")
                ) {
                    StudentAvatar(
                        name = friend.name,
                        avatarColor = Color(friend.avatarColorHex),
                        size = 52.dp,
                        showOnlineDot = friend.isOnline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = friend.name.split(" ").firstOrNull() ?: friend.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }

            // Invite / Add friend button
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSeeAllClick() }
                        .testTag("add_friend_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(CardSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add friend",
                            tint = BrandPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Add",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryFilterChips(
    selectedCategory: SubjectCategory,
    onCategorySelect: (SubjectCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Category",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(SubjectCategory.values(), key = { it.name }) { category ->
                val isSelected = category == selectedCategory
                Surface(
                    onClick = { onCategorySelect(category) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) BrandYellow else CardWhite,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) BrandYellowDark else DividerColor
                    ),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("category_chip_${category.name}")
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.displayName,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}
