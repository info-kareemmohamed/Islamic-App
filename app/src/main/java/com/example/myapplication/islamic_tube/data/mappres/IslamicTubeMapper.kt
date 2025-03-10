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


fun IslamicTubeDto.toSections(): List<Section> {
    return sections.map { it.toSection() }
}


fun SectionDto.toSection(): Section {
    return Section(
        name = title,
        categories = categories.map { it.toCategory() }
    )
}


fun CategoryDto.toCategory(): Category {
    return Category(
        name = title,
        imageUrl = url,
    )
}


fun ItemDto.toVideo(): Video {
    return Video(
        title = title,
        url = url
    )
}

fun List<ItemDto>.toPlaylist(name: String): Playlist {
    return Playlist(
        name = name,
        videos = map { it.toVideo() }
    )
}

