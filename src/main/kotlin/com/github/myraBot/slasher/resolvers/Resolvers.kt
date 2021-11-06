package com.github.myraBot.slasher.resolvers

import com.github.myraBot.diskord.common.entities.User
import com.github.myraBot.diskord.common.entities.channel.TextChannel
import com.github.myraBot.diskord.common.entities.guild.Member
import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.slasher.MultiWord
import com.github.myraBot.slasher.resolvers.impl.*
import kotlin.reflect.KClassifier
import kotlin.reflect.KParameter

object Resolvers {

    private val map: MutableMap<KClassifier, Resolver<*>> = mutableMapOf()

    fun registerDefaultResolvers() {
        map[MultiWord::class] = MultiWordResolver()
        map[Int::class] = IntResolver()
        map[Long::class] = LongResolver()
        map[Float::class] = FloatResolver()
        map[Double::class] = DoubleResolver()

        map[User::class] = UserResolver()
        map[Member::class] = MemberResolver()
        map[TextChannel::class] = TextChannelResolver()
    }

    suspend fun resolve(ctx: CommandContext, parameter: KParameter, arg: String?, args: String?): Arg<*> {
        val clazz = parameter.type.arguments.first().type?.classifier
        val resolver = map[clazz] ?: throw Exception("The type ${parameter::class.java.genericSuperclass.typeName} hasn't a registered resolver")

        return if (resolver::class == MultiWordResolver::class) resolver.resolve(ctx, args)
        else resolver.resolve(ctx, arg)
    }

}
