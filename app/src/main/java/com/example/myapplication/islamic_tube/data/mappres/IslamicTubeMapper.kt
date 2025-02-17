package com.example.myapplication.islamic_tube.data.mappres

import com.example.myapplication.islamic_tube.data.networking.dto.CategoryDto
import com.example.myapplication.islamic_tube.data.networking.dto.SubCategoryDto
import com.example.myapplication.islamic_tube.data.networking.dto.VideoDto
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.model.SubCategory
import com.example.myapplication.islamic_tube.domain.model.Video

fun VideoDto.toVideo(): Video = Video(
    title = title,
    url = url
)

fun SubCategoryDto.toSubCategory(): SubCategory = SubCategory(
    name = name,
    videos = videos.map { it.toVideo() }
)

fun CategoryDto.toCategory(): Category = Category(
    name = name,
    subCategories = subCategories.map { it.toSubCategory() }
)