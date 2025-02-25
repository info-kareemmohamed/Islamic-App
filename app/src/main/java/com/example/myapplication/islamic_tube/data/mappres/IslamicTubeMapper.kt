package com.example.myapplication.islamic_tube.data.mappres

import com.example.myapplication.islamic_tube.data.local.VideoEntity
import com.example.myapplication.islamic_tube.data.networking.dto.IslamicTubeDto
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.model.SubCategory
import com.example.myapplication.islamic_tube.domain.model.Video


fun VideoEntity.toVideo(): Video = Video(
    title = title,
    url = url,
)

fun Video.toVideoEntity(): VideoEntity = VideoEntity(
    title = title,
    url = url
)

fun IslamicTubeDto.toCategories(): List<Category> {
    val groupedItems = items.groupBy { it.category }
    return sections.map { section ->
        Category(
            name = section.title,
            subCategories = section.categories.map { category ->
                SubCategory(
                    imageUrl = category.url,
                    name = category.title,
                    videos = groupedItems[category.title]
                        ?.map { Video(it.title, it.url) }
                        ?: emptyList()
                )
            }
        )
    }
}
