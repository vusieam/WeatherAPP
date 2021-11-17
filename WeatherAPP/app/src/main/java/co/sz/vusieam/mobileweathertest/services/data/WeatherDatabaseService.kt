package co.sz.vusieam.mobileweathertest.services.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import co.sz.vusieam.mobileweathertest.models.entities.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.util.concurrent.Executors

@Database(
    entities =
    [
        WeatherEntity::class, CityEntity::class, ExtendedWeatherEntity::class
    ],
    version = 1, exportSchema = false)
abstract class WeatherDatabaseService : RoomDatabase() {
    abstract fun getWeatherServiceDao() : WeatherServiceDao

    companion object {

        @Volatile
        private var instance : WeatherDatabaseService? = null
        private val LOCK = Any()

        fun  getDatabaseInstance(context: Context):WeatherDatabaseService{
            return instance?: synchronized(LOCK){
                instance?:buildDatabaseStore(context).also{ instance = it }
            }
        }

        val paraphrase = SQLiteDatabase.getBytes("WeatherApp@)@!".toCharArray())
        val factory = SupportFactory(paraphrase)

        private fun buildDatabaseStore(context: Context) : WeatherDatabaseService{
            return Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabaseService::class.java,
                "MobileWeatherAppDb.db3"
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .addCallback(object : RoomDatabase
                .Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor()
                            .execute(object : Runnable {
                                override fun run() {
                                }
                            })
                    }
                })
                .openHelperFactory(factory)
                .build().also { Log.d("<DEV>", it.openHelper.writableDatabase.path ) }
        }
    }

}