package com.example.emotiontracking

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.emotiontracking.database.Mood
import com.example.emotiontracking.database.MoodDatabase
import com.example.emotiontracking.database.MoodDatabaseDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception
import kotlin.jvm.Throws

// Android test sınıfı olduğunu belirtiriyoruz
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var databaseAccessObject: MoodDatabaseDao
    private lateinit var database: MoodDatabase

    @Before
    fun createDatabase() {
        val connect = InstrumentationRegistry.getInstrumentation().targetContext

        // Veritabanını diskte değil ram de çalıştırıyoruz
        // Çünkü sadece test amaçlı geçici bir kod ve verilerin daimi hafızada tutulmasına gerek yok
        // Normalde veritabanı işlemleri diskte çalışır ki veriler cihazda daimi kalsın
        database = Room.inMemoryDatabaseBuilder(connect, MoodDatabase::class.java)
                //
                .allowMainThreadQueries().build()
        databaseAccessObject = database.databaseAccessObject

    }

    // Testten sonra veritabanını kapatmalıyız yoksa açık kalıp ram i kasarlar
    @After
    @Throws(Exception::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun addAndReadMood(){
        // MoodDatabaseDao interface indeki fonksiyonlarımız suspend ile asenkron çalışaak biçimde
        // ayarlandıysa eğer kodumuz aşağıdaki gibi çalışacaktır. Fakat suspend olmadan senkron bir
        // şekilde çalıştırmak istersek runBlocking içerisindeki kodlarımızı bu fonksiyondan kurtarıp
        // sade biçimde çalıştırmalıyız. Yani runBocking kodlarımızı asenkron çalıştırmamızı sağlar.
        runBlocking {
            val emotion = Mood()
            databaseAccessObject.insert(emotion)
            val endMood = databaseAccessObject.getLatestMood()
            // Asıl test edilen kısım burası
            // endMood içerisi null ise değeri -1 olmayacaktır. Bu durumda veritabanına veri eklenememiştir.
            assertEquals(endMood?.mood, -1)
        }

        // asenkron: birbirini kesmeyen yani bir sonraki işlem için bir önceki işlemin bitmesini
        // beklememize gerek olmayan işlemlerdir

    }
}