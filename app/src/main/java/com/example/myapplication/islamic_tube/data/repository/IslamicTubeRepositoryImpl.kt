package com.example.myapplication.islamic_tube.data.repository

import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.Result
import com.example.myapplication.islamic_tube.data.local.CategoryDao
import com.example.myapplication.islamic_tube.data.mappres.toCategory
import com.example.myapplication.islamic_tube.data.mappres.toSubCategory
import com.example.myapplication.islamic_tube.data.mappres.toVideo
import com.example.myapplication.islamic_tube.data.mappres.toVideoEntity
import com.example.myapplication.islamic_tube.data.networking.dto.CategoryDto
import com.example.myapplication.islamic_tube.data.networking.dto.SubCategoryDto
import com.example.myapplication.islamic_tube.data.networking.dto.VideoDto
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.model.SubCategory
import com.example.myapplication.islamic_tube.domain.model.Video
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IslamicTubeRepositoryImpl(
    private val httpClient: HttpClient,
    private val categoryDao: CategoryDao,
) : IslamicTubeRepository {
    override suspend fun getIslamicTubeVideos(): Result<List<Category>, NetworkError> {
        // TODO: implement this
        //  return safeCall<Category> { httpClient.get(urlString = "example.com") }

        //Dummy Data
        return Result.Success(getDummyData().map { it.toCategory() })
    }

    override suspend fun getSubCategoryFromNetwork(
        categoryName: String,
        subCategoryName: String
    ): Result<SubCategory, NetworkError> {

        // TODO: implement this
        //  return safeCall<Category> { httpClient.get(urlString = "example.com") }

        //Dummy Data
        val dummyData = getDummyData()
        val category = dummyData.firstOrNull { it.name == categoryName }
            ?: return Result.Error(NetworkError.UNKNOWN)
        val subCategory = category.subCategories.firstOrNull { it.name == subCategoryName }
            ?: return Result.Error(NetworkError.UNKNOWN)
        return Result.Success(subCategory.toSubCategory())
    }

    override suspend fun getSubCategoryFromLocal(categoryName: String): List<Video> =
        categoryDao.getCategoryByName(categoryName)?.videoEntities?.map { it.toVideo() }
            ?: emptyList()

    override fun observeCategoryNamesAndFirstVideo(): Flow<Pair<List<Video>, List<String>>> =
        categoryDao.observeCategoryNameAndFirstVideo().map { list ->
            val names = list.map { it.name }
            val videos = list.map { it.firstVideo.toVideo() }
            videos to names
        }

    override fun observeCategoryNamesByVideoUrl(videoUrl: String): Flow<List<String>> =
        categoryDao.observeCategoryNamesByVideoUrl(videoUrl)


    override suspend fun createEmptyCategory(categoryName: String) =
        categoryDao.createCategoryIfNotExists(categoryName)


    override fun observeCategoryNames(): Flow<List<String>> =
        categoryDao.observeCategoryNames()


    override suspend fun upsertVideoCategories(
        oldCategoryNames: List<String>,
        newCategoryNames: List<String>,
        video: Video
    ) = categoryDao.upsertVideoCategories(oldCategoryNames, newCategoryNames, video.toVideoEntity())


    //TODO: Remove this function
    private fun getDummyData(): List<CategoryDto> {
        return listOf(
            CategoryDto(
                name = "رمضان ٢٣",
                subCategories = listOf(
                    SubCategoryDto(
                        name = "كل يوم آية",
                        videos = listOf(
                            VideoDto(
                                title = "كل يوم آية 1 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=tHvfqbfQuXg&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=1&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 2 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=iQQGgB9nFO0&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=2&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 3 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=bXTxrkTk5EE&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=3&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 4 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=TVSDsp6iWQo&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=4&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 5 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=myoJ8nRW29Y&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=5&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 6 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=tEC_Z_Z2laE&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=6&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 7 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=3Ng_-lqUwyc&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=7&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 8 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=BeWKKN1MGPk&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=8&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 9 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=j4Mp5QR9Z4w&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=9&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 10 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=TT3rwIP2H2M&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=10&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 11 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=WtqX560B8bA&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=11&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 12 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=CyKGUmA7sOs&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=12&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 13 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=mlsE_TRS0ww&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=13&pp=iAQB"
                            ),
                            VideoDto(
                                title = "هاااام لكل واحد انتكس - كل يوم آية 14 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=PL8L6bQzkPQ&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=14&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 15 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=t6wqbzRYPrU&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=15&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 16 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=cdo9yk4prpA&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=16&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 17 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=TCM7e_eX97o&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=17&pp=iAQB"
                            ),
                            VideoDto(
                                title = "هاااااام لكل مسلم ومسلمة - كل يوم آية 18 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=QyZud9Eb84Q&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=18&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 19 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=xf-CkzG3j1E&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=19&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 20 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=6_TXsFBX2oI&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=20&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 21 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=d5H1oS6xvL0&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=21&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 22 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=8Q6ZO2VhltU&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=22&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 23 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=-M5dADT_T4U&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=23&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 24 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=YWug_3TmGFU&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=24&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 25 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=TkXj2E1YN0w&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=25&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 26 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=tLz6d6rHXhI&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=26&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 27 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=_jzHxjlYNJc&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=27&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 28 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=wPNFt5Gbq2A&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=28&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 29 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=rpnC8byyLSE&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=29&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كل يوم آية 30 - محمد الغليظ",
                                url = "https://www.youtube.com/watch?v=E3LqdmRJKy8&list=PLgqtKVxUxe2qJODDwEX6LA3-euaMbQb7Y&index=30&pp=iAQB"
                            )
                        )
                    )
                )
            ),
            CategoryDto(
                name = "تثبيت حفظ القران",
                subCategories = listOf(
                    SubCategoryDto(
                        name = "جزء عم",
                        videos = listOf(
                            VideoDto(
                                title = "تثبيت وتربيط سورة النبأ",
                                url = "https://www.youtube.com/watch?v=Dbk6gY8MIh0&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=1&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة النازعات",
                                url = "https://www.youtube.com/watch?v=jQlrHB8U2pU&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=2&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة عبس",
                                url = "https://www.youtube.com/watch?v=bsbCPAWvcMQ&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=3&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة التكوير",
                                url = "https://www.youtube.com/watch?v=f7g2OtYE1jM&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=4&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الانفطار",
                                url = "https://www.youtube.com/watch?v=lZvr5uhAj9Q&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=5&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة المطففين",
                                url = "https://www.youtube.com/watch?v=IQOVYfj2G5Q&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=6&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الانشقاق",
                                url = "https://www.youtube.com/watch?v=fppTTiyKLys&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=7&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة البروج",
                                url = "https://www.youtube.com/watch?v=CEjD9L5lxFo&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=8&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الطارق",
                                url = "https://www.youtube.com/watch?v=ggLoS69mykE&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=9&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الاعلى",
                                url = "https://www.youtube.com/watch?v=Ds5-XZYbqBY&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=10&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الغاشية",
                                url = "https://www.youtube.com/watch?v=E5QUHl6mzlo&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=11&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الفجر",
                                url = "https://www.youtube.com/watch?v=X_RbwWi2094&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=12&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة البلد",
                                url = "https://www.youtube.com/watch?v=L8pF2e646aM&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=13&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الشمس",
                                url = "https://www.youtube.com/watch?v=SDrrroVH13g&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=14&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الليل",
                                url = "https://www.youtube.com/watch?v=jMqooZ92t_w&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=15&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط وتدبر سورة الضحى",
                                url = "https://www.youtube.com/watch?v=KPqQhz4IacY&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=16&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط وتدبر سورة الشرح",
                                url = "https://www.youtube.com/watch?v=lOpeFIvEKn0&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=17&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط وتدبر سورة التين",
                                url = "https://www.youtube.com/watch?v=6Q9HumDetao&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=18&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة العلق",
                                url = "https://www.youtube.com/watch?v=wGZA2pUBLdI&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=19&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط وتدبر سورة القدر",
                                url = "https://www.youtube.com/watch?v=Tb10I5mxzW4&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=20&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة البينة",
                                url = "https://www.youtube.com/watch?v=BBQr7R7bZQk&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=21&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط و تدبر سورة الزلزلة",
                                url = "https://www.youtube.com/watch?v=AsFhMZE5o1U&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=22&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط و تدبر سورة العاديات",
                                url = "https://www.youtube.com/watch?v=zLStmjf3Xww&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=23&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط و تدبر سورة القارعة",
                                url = "https://www.youtube.com/watch?v=E12ENYhmd2s&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=24&pp=iAQB"
                            )
                        )
                    ),
                    SubCategoryDto(
                        name = "جزء تبارك",
                        videos = listOf(
                            VideoDto(
                                title = "تثبيت وتربيط سورة الملك كاملة",
                                url = "https://www.youtube.com/watch?v=MAM61xvcVgo&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=1&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة القلم . كاملة",
                                url = "https://www.youtube.com/watch?v=07TJ2RZ24yo&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=2&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الحاقة. كاملة",
                                url = "https://www.youtube.com/watch?v=kwAfscCMRTk&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=3&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة المعارج. كاملة",
                                url = "https://www.youtube.com/watch?v=eWewyR_YEEo&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=4&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة نوح . كاملة",
                                url = "https://www.youtube.com/watch?v=yBk0nzgZya8&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=5&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الجن. كاملة",
                                url = "https://www.youtube.com/watch?v=n0oS5DLSzZU&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=6&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة المزمل . كاملة .",
                                url = "https://www.youtube.com/watch?v=RGjoQWgTaXM&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=7&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة المدثر. كاملة.",
                                url = "https://www.youtube.com/watch?v=1mteHe4voo0&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=8&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة القيامة. كاملة .",
                                url = "https://www.youtube.com/watch?v=xrZ02niZfLk&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=9&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الانسان. كاملة.",
                                url = "https://www.youtube.com/watch?v=WCZ50lYj-R8&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=10&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة المرسلات. كاملة.",
                                url = "https://www.youtube.com/watch?v=yD236I7By2I&list=PL0146TQmugPBvJPtPqtrmRI2iozkjfUEb&index=11&pp=iAQB"
                            )
                        )
                    ),
                    SubCategoryDto(
                        name = "جزء قد سمع",
                        videos = listOf(
                            VideoDto(
                                title = "تثبيت وتربيط سورة المجادلة[ ايه ١ حتى ايه ١٣ ]",
                                url = "https://www.youtube.com/watch?v=qyuxUQLQ2o8&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=1&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة المجادلة [ من ايه ١٤ إلى آخر السورة ]",
                                url = "https://www.youtube.com/watch?v=1BbpWiBUnts&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=2&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط الربع الأول من سورة الحشر[ من ايه١ حتى ايه ١٠]",
                                url = "https://www.youtube.com/watch?v=gfCwXvlEX78&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=3&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط الربع الثانى من سورة الحشر[ من ايه ١١ حتى آخر السورة ]",
                                url = "https://www.youtube.com/watch?v=EMO5a8Kagdc&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=4&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط المقطع الأول من سورة الممتحنة من [ أية ١ حتى أية ٦ ]",
                                url = "https://www.youtube.com/watch?v=I1TvIOp6PsA&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=5&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط المقطع الثاني من سورة الممتحنة من أية[ ٧ حتى آخر السورة ]",
                                url = "https://www.youtube.com/watch?v=Np5oyEEOUu4&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=6&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت و تربيط سورة الصف كاملة",
                                url = "https://www.youtube.com/watch?v=gDTXHPb_oZk&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=7&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الجمعة كاملة",
                                url = "https://www.youtube.com/watch?v=oXL1gIE1_8s&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=8&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة المنافقون كاملة",
                                url = "https://www.youtube.com/watch?v=_WTZq8XAEW8&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=9&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط الصفحة الأولى من سورة التغابن من[ ايه ١ حتى ايه ٩ ]",
                                url = "https://www.youtube.com/watch?v=xjeq15QRBC0&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=10&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط الوجه الثانى من سورة التغابن[ من ايه ١٠ حتى آخر السورة ]",
                                url = "https://www.youtube.com/watch?v=ATQ7H3plXpk&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=11&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة الطلاق كاملة",
                                url = "https://www.youtube.com/watch?v=sDTCuYPrwwU&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=12&pp=iAQB"
                            ),
                            VideoDto(
                                title = "تثبيت وتربيط سورة التحريم كاملة",
                                url = "https://www.youtube.com/watch?v=S2kjW3wxD7Y&list=PL0146TQmugPB_PSU-_VPujOzICa6p9B3U&index=13&pp=iAQB"
                            )
                        )
                    )
                )
            ),
            CategoryDto(
                name = "هموم الشباب",
                subCategories = listOf(
                    SubCategoryDto(
                        name = "هموم الشباب",
                        videos = listOf(
                            VideoDto(
                                title = "لماذا هذه السلسلة؟",
                                url = "https://www.youtube.com/watch?v=Q_RKRIhNwUQ&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=1&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أترك المعصية؟",
                                url = "https://www.youtube.com/watch?v=o8PP5NxefHk&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=2&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أُرضي أبَوَي؟",
                                url = "https://www.youtube.com/watch?v=zMWU3NoGxe4&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=3&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أجدُ عملاً مناسباً؟",
                                url = "https://www.youtube.com/watch?v=pRwL77nI2y0&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=4&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف لي بالزواج؟",
                                url = "https://www.youtube.com/watch?v=q5nYNNFv5fc&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=5&pp=iAQB"
                            ),
                            VideoDto(
                                title = "ماذا أقرأ لأفهم ديني؟",
                                url = "https://www.youtube.com/watch?v=asgrdqnTwJo&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=6&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أجمعُ بين الدنيا و الآخرة؟",
                                url = "https://www.youtube.com/watch?v=EWfn9V1vRms&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=7&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أجمع بين الجِد والهَزل؟ - د.محمد خير الشعال",
                                url = "https://www.youtube.com/watch?v=Hj4rdNcRqK0&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=8&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أختار تخصّصي الجامعي؟ - د.محمد خير الشعال",
                                url = "https://www.youtube.com/watch?v=x0qEZWyUXpY&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=9&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف يَلِين قلبي؟ - د.محمد خير الشعال",
                                url = "https://www.youtube.com/watch?v=UrbEA_088D8&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=10&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أخدم بلدي؟ - د.محمد خير الشعال",
                                url = "https://www.youtube.com/watch?v=g6vpGfHY3Ks&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=11&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أُسَيطر على الغضب؟ - د.محمد خير الشعال",
                                url = "https://www.youtube.com/watch?v=mE9CrJHi7aA&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=12&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أنجُو من الفِتن؟ - د.محمد خير الشعال",
                                url = "https://www.youtube.com/watch?v=8SbOwCRKHuc&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=13&pp=iAQB"
                            ),
                            VideoDto(
                                title = "كيف أُنظِّم وقتي؟ - د.محمد خير الشعال",
                                url = "https://www.youtube.com/watch?v=sa3Hdj3g8KY&list=PLMPqxr1nu2ZfhlCwSs-NM1aD8isu8KFNY&index=14&pp=iAQB"
                            )
                        )
                    )
                )
            ),
            CategoryDto(
                name = "سمير مصطفي",
                subCategories = listOf(
                    SubCategoryDto(
                        name = "سلسلة القضاء والقدر",
                        videos = listOf(
                            VideoDto(
                                title = "•l█l• مسجد الحق ۞ عقيدة القضاء والقدر جـ 2 ۞ للشيخ سمير مصطفى •l█l•",
                                url = "https://www.youtube.com/watch?v=tldDnDX5UKM&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=1&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ عقيدة القضاء والقدر ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=qSJ_4Y0jRxE&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=2&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ ثمرات الايمان بالقضاء والقدر ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=YAKQEmNC944&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=3&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ واجبات القدر واجب الرضا ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=hGJe9SAO49c&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=4&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ واجبات القدر واجب الرضا 2 ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=25PUsDmDAoU&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=5&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ واجبات القدر واجب الرضا 3 ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=6Y5KQuc2xQI&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=6&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ مقدمة لمراتب القدر جـ3 ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=kzIQBAeCqrs&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=7&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ واجبات القدر واجب الرضا 4 ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=y2i_N-Cn6Y0&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=8&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ واجبات القدر( واجب الصبر 1 ) ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=26XjdSBOtuk&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=9&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ واجبات القدر( واجب الصبر 2 ) ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=UqXhjlq5D5A&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=10&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ كتابة المقادير ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=9Ob-OoiuWG4&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=11&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ مراتب القدر جـ2 ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=3G1TcVnYZTE&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=12&pp=iAQB"
                            ),
                            VideoDto(
                                title = "مسجد الحق ۞ أركــــان القـــــــدر ۞ للشيخ سمير مصطفى",
                                url = "https://www.youtube.com/watch?v=Fk0fcb5qXy4&list=PLSUcSqxe9RhyS7_hi2I5D7CH2sACsPjFW&index=13&pp=iAQB"
                            )
                        )
                    )
                )
            )
        )
    }

}