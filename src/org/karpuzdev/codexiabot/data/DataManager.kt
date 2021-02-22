package org.karpuzdev.codexiabot.data

import me.koply.kcommando.DataManager as KDataManager
import me.koply.kcommando.Parameters
import org.json.JSONArray
import org.json.JSONObject
import org.karpuzdev.codexiabot.objects.User
import org.karpuzdev.codexiabot.objects.ProjectMessage
import org.karpuzdev.codexiabot.util.Util
import net.dv8tion.jda.api.entities.User as JDAUser
import java.io.File
import kotlin.properties.Delegates
import kotlin.system.exitProcess

object DataManager {

    private val levelStages = ArrayList<Int>()
    private val levelImages = ArrayList<String>()
    private var projectsChannel by Delegates.notNull<Long>()

    val users = HashMap<Long, User>()
    val projects = HashMap<Long, ProjectMessage>()

    class ManagerWrapper(file: File, params: Parameters) : KDataManager(file, params) {
        private fun preConfigCheck(): JSONObject {
            val configFile = File("./config.json")
            if (!configFile.exists()) {
                configFile.createNewFile()
                println("Config dosyası oluşturuldu. Lütfen doldurup botu tekrar başlatın.")
                exitProcess(-1)
            } else {
                val configStr = Util.readFile(configFile)
                if (configStr.isNullOrEmpty()) {
                    println("Config boş. Lütfen doldurup botu tekrar başlatın.")
                    exitProcess(-1)
                }
                return JSONObject(configStr)
            }
        }

        override fun initDataFile(): JSONObject {
            val configJson = preConfigCheck()
            configJson.optJSONArray("levelStages")?.let { it.forEach { obj -> levelStages.add(obj as Int) } }
            configJson.optJSONArray("levelImages")?.let { it.forEach { obj -> levelImages.add(obj as String) } }
            configJson.optLong("projectsChannel").let { projectsChannel = it }

            val rootJson = super.initDataFile()
            // kcommando data dosyasıyla istediklerini yapıyo

            rootJson.optJSONArray("users")?.let {
                it.forEach { inline ->
                    val ino = inline as JSONObject
                    users[ino["id"] as Long] = User(ino["id"] as Long, ino["xp"] as Int)
                }
            }
            rootJson.optJSONArray("projectMessages")?.let {
                it.forEach { inline ->
                    val ino = inline as JSONObject
                    projects[ino["messageid"] as Long] = ProjectMessage(ino["messageid"] as Long, ino["authorid"] as Long, ino["gainedXp"] as Int)
                }
            }

            return rootJson
        }

        override fun pushToJson(): JSONObject {
            val rootJson =  super.pushToJson()
            val usersArray = JSONArray()
            users.forEach {
                usersArray.put(JSONObject()
                        .put("id", it.key)
                        .put("xp", it.value.xp))
            }
            rootJson.put("users", usersArray)

            val projectsArray = JSONArray()
            projects.forEach {
                projectsArray.put(JSONObject()
                        .put("messageid", it.key)
                        .put("authorid", it.value.authorID)
                        .put("gainedXp", it.value.gainedXP))
            }
            rootJson.put("projectMessages", projectsArray)
            return rootJson
        }
    }

    /**
     * Projede kullanıcı dataları bu methodla çekilecek.
     */
    fun getUser(jdauser: JDAUser): User {
        return if (users.containsKey(jdauser.idLong)) {
            users[jdauser.idLong]!!
        } else {
            val newuser = User(jdauser.idLong, 0)
            users[jdauser.idLong] = newuser
            newuser
        }
    }

    /**
     * event içinde bu method ile eklenen reacta göre kişiye xp eklenecek
     * !.let {} ile null olmadığı durumda çalışması sağlanıyor
     */
    fun getProjectMessage(id: Long): ProjectMessage? {
        return projects[id]
    }
}