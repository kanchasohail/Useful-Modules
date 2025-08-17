package com.tools.gamebooster.model.room_database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tools.gamebooster.model.room_database.chats.Chat
import com.tools.gamebooster.model.room_database.chats.ChatsDao
import com.tools.gamebooster.model.room_database.games.DeletedGame
import com.tools.gamebooster.model.room_database.games.Game
import com.tools.gamebooster.model.room_database.games.GamesDao
import java.io.ByteArrayOutputStream

@Database(entities = [Game::class, Chat::class, DeletedGame::class], version = 2)
@TypeConverters(Converters::class)
abstract class GameBoosterDatabase : RoomDatabase() {

    abstract fun gamesDao(): GamesDao
    abstract fun chatsDao(): ChatsDao

    companion object {
        @Volatile
        private var INSTANCE: GameBoosterDatabase? = null

        fun getInstance(context: Context): GameBoosterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameBoosterDatabase::class.java,
                    "game_booster_database.db"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }

}

class Converters {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray): ImageBitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    }

    @TypeConverter
    fun toByteArray(bitmap: ImageBitmap): ByteArray {
        return imageBitmapToByteArray(bitmap)
    }

    private fun imageBitmapToByteArray(bitmap: ImageBitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        //Create DeletedGame Table
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS DeletedGame (
                package_name TEXT NOT NULL PRIMARY KEY
            )
        """)
    }
}