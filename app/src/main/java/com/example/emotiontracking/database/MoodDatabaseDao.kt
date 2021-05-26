package com.example.emotiontracking.database

import androidx.lifecycle.LiveData
import androidx.room.*

// Veritabanı erişim nesnesinin kalıbını oluşturuyoruz
@Dao
interface MoodDatabaseDao {
    // Veritabanları asenkron çalışırlar. Bunun için fonksiyonların başına suspend etiketini ekleriz
    // Suspend etiketi olmadan senkron biçimde çalışmayı destekleyen DatabaseTest classını
    // çalıştırabiliriz fakat asenkron biçiminde bu test class ı çalışmayacaktır
    @Insert
    suspend fun insert(mood: Mood)
    // Eğer tek tek for döngüsü ile değilde tümden eklemek istersek aşağıdaki gibi kullanmalıyız
    // Veriyi tümden eklediğimiz için olurda bir karışıklık olursa(conflic) o zaman ne yapılacağını seçmemiz gerekebilir.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert2(moods: List<Mood>)

    // Tüm verileri id ye göre büyükten küçüğe(sondan başa) livedata olarak listeler
    // DESC vermemiş olsaydık ASC yazmasak dahi ASC olarak sıralanacaktı
    @Query("SELECT * FROM mood_tracking_table ORDER BY id DESC")
    fun getAllMood(): LiveData<List<Mood>>
    // fun getAllMood(): List<Mood>

    @Query("SELECT * from mood_tracking_table WHERE id = :key")
    suspend fun get(key: Long): Mood?
    // Sona eklenen ? nin anlamı verinin null da dönebileceğini belirtir

    // Son eklenen duygu durumunu getirir
    @Query("SELECT * FROM mood_tracking_table ORDER BY id DESC LIMIT 1")
    suspend fun getLatestMood(): Mood?

    @Query("SELECT * FROM mood_tracking_table WHERE id = :key")
    fun getId(key: Long): LiveData<Mood?>

    // Tüm veriyi siler
    @Query("DELETE FROM mood_tracking_table")
    suspend fun clear()

    // Tek duygu durumunu siler
    @Query("DELETE FROM mood_tracking_table WHERE id = :key")
    suspend fun deleteOneMood(key: Long)

    // Yukarıdaki işlem ile aynı
    @Delete
    suspend fun del(mood: Mood)

    // Güncellenecek olan duygu durumunu günceller
    @Update
    suspend fun update(mood: Mood)

    // Güncelleme ve silme işlemleri için geri dönüş parametresine gerek yok

    // Livedata lar asenkron çalıştıkları için onların asenkron olduklarını belirtmeyebiliriz
}