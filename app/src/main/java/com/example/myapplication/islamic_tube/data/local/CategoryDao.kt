package com.example.myapplication.islamic_tube.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertCategory(category: CategoryEntity): Long

    @Query("SELECT * FROM categories WHERE name = :name")
    suspend fun getCategoryByName(name: String): CategoryEntity?

    @Query("SELECT name FROM categories WHERE videoEntities LIKE '%' || :videoUrl || '%'")
    fun observeCategoryNamesByVideoUrl(videoUrl: String): Flow<List<String>>

    @Query("SELECT * FROM categories WHERE videoEntities LIKE '%' || :videoUrl || '%'")
    fun getCategoriesContainingVideoUrl(videoUrl: String): List<CategoryEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM categories WHERE videoEntities LIKE '%' || :videoUrl || '%')")
    suspend fun isVideoInAnyCategory(videoUrl: String): Boolean

    @Query("SELECT name FROM categories")
    fun observeCategoryNames(): Flow<List<String>>

    @Query(
        """
    SELECT name, json_extract(videoEntities, '$[0]') as firstVideo 
    FROM categories
    WHERE json_array_length(videoEntities) > 0
    """
    )
    fun observeCategoryNameAndFirstVideo(): Flow<List<CategoryNameAndFirstVideoUrl>>


    @Transaction
    suspend fun createCategoryIfNotExists(categoryName: String) {
        getCategoryByName(categoryName) ?: upsertCategory(CategoryEntity(categoryName, emptyList()))
    }

    @Transaction
    suspend fun upsertVideoCategories(
        oldCategoryNames: List<String>,
        newCategoryNames: List<String>,
        videoEntity: VideoEntity
    ) {
        // Remove video from old categories
        removeVideoFromCategories(videoEntity)

        // Add video to new categories
        newCategoryNames.forEach { categoryName ->
            val category = getCategoryByName(categoryName)
            upsertCategory(
                category?.copy(videoEntities = category.videoEntities + videoEntity)
                    ?: CategoryEntity(name = categoryName, videoEntities = listOf(videoEntity))
            )
        }
    }

    @Transaction
    suspend fun removeVideoFromCategories(videoEntity: VideoEntity) {
        val categories = getCategoriesContainingVideoUrl(videoEntity.url)
        categories.forEach { category ->
            val updatedVideos = category.videoEntities.filter { it.url != videoEntity.url }
            upsertCategory(category.copy(videoEntities = updatedVideos))
        }
    }
}