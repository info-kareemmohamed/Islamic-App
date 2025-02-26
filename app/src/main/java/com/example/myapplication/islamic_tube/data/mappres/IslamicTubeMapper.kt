package com.example.myapplication.islamic_tube.data.mappres

import com.example.myapplication.islamic_tube.data.local.CategoryEntity
import com.example.myapplication.islamic_tube.data.local.VideoEntity
import com.example.myapplication.islamic_tube.data.networking.dto.CategoryDto
import com.example.myapplication.islamic_tube.data.networking.dto.IslamicTubeDto
import com.example.myapplication.islamic_tube.data.networking.dto.ItemDto
import com.example.myapplication.islamic_tube.data.networking.dto.SectionDto
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.model.Playlist
import com.example.myapplication.islamic_tube.domain.model.Section
import com.example.myapplication.islamic_tube.domain.model.Video


fun VideoEntity.toVideo(): Video = Video(
    title = title,
    url = url,
)

fun Video.toVideoEntity(): VideoEntity = VideoEntity(
    title = title,
    url = url
)

fun CategoryEntity.toPlaylist(): Playlist = Playlist(
    name = name,
    videos = videoEntities.map { it.toVideo() }
)





fun ItemDto.toVideo(): Video = Video(
    title = title,
    url = url
)

fun CategoryDto.toCategory(): Category = Category(
    name = title,
    imageUrl = url
)

fun SectionDto.toSection(): Section = Section(
    name = title,
    categories = categories.map { categoryDto -> categoryDto.toCategory() }
)

fun IslamicTubeDto.toSections(): List<Section> = sections.map { it.toSection() }


fun IslamicTubeDto.toPlaylists(): List<Playlist> =
    items.groupBy { it.category }
        .map { (categoryName, items) ->
            Playlist(
                name = categoryName,
                videos = items.map {videoDto ->
                    videoDto.toVideo() }
            )
        }




