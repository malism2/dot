package com.malism.dot.dao

import com.malism.dot.bean.Config
import com.malism.dot.utils.dbQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert

class ConfigDao(database: Database) {
    object Configs: IntIdTable("configs") {
        val name = varchar("name", length = 255).uniqueIndex()
        val value = varchar("value", length = 1024)
        val value1 = varchar("value1", length = 1024)
        val value2 = varchar("value2", length = 1024)
        val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
        val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
    }
    init {
        transaction(database) {
            SchemaUtils.create(Configs)
        }
    }

    suspend fun get(name: String): Config? {
        return dbQuery {
            Configs.selectAll()
                .where{ Configs.name eq name }
                .limit(1)
                .map {
                    Config(
                        it[Configs.name],
                        it[Configs.value],
                        it[Configs.value1],
                        it[Configs.value2]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun set(config: Config) {
        dbQuery {
            Configs.upsert(onUpdateExclude = mutableListOf<Column<*>>(Configs.createdAt)) {
                it[Configs.name] = config.name
                it[Configs.value] = config.value
                it[Configs.value1] = config.value1
                it[Configs.value2] = config.value2
                it[Configs.updatedAt] = CurrentDateTime
            }
        }
    }

    suspend fun delete(name: String) {
        dbQuery {
            Configs.deleteWhere { Configs.name eq name }
        }
    }
}