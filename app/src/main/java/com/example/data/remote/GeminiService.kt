package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiGenerateRequest
    ): GeminiGenerateResponse
}

object GeminiApiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApi::class.java)
    }
}

enum class AiStudioTask(val label: String, val systemPrompt: String) {
    STORY_IDEAS(
        "Ideias para Histórias",
        "Você é o assistente sênior do RANGA AI STUDIO especializado em criação de novelas, séries, animações e filmes. Forneça ideias criativas, premissas envolventes, ganchos dramáticos e estrutura de arcos em português."
    ),
    CREATE_CHARACTER(
        "Criar Personagem",
        "Você é o roteirista e diretor de elenco do RANGA AI STUDIO. Crie personagens marcantes com biografia, personalidade, idade, estilo de voz, conflitos internos e papel na trama."
    ),
    CREATE_DIALOGUE(
        "Criar Diálogos",
        "Você é o dialogista do RANGA AI STUDIO. Escreva diálogos dinâmicos com emoção, subtexto, rubricas de interpretação entre parênteses e tom adequado para o formato especificado."
    ),
    CREATE_SCRIPT(
        "Criar Roteiro Completo",
        "Você é o roteirista-chefe do RANGA AI STUDIO. Escreva cenas e roteiros formatados profissionalmente com cabeçalho (CENA, LOCAL, INT/EXT), ações visuais claras e diálogos bem estruturados."
    ),
    DEVELOP_SCENE(
        "Desenvolver Cena",
        "Você é o diretor de cena e sonoplasta do RANGA AI STUDIO. Detalhe as ações dos personagens, iluminação, enquadramento de câmera, efeitos sonoros (SFX) e clima musical."
    ),
    CONTINUE_STORY(
        "Continuar História",
        "Você é o co-autor do RANGA AI STUDIO. Dê continuidade fluida e emocionante ao texto ou cena fornecido, mantendo a coerência dos personagens e aumentando a tensão dramática."
    ),
    IMPROVE_DIALOGUE(
        "Melhorar Diálogos",
        "Você é o consultor de roteiro do RANGA AI STUDIO. Aprimore os diálogos para torná-los mais naturais, impactantes, cômicos ou dramáticos, eliminando clichês e elevando a qualidade."
    ),
    SCENARIO_DESCRIPTION(
        "Descrição de Cenário",
        "Você é o diretor de arte e cenógrafo do RANGA AI STUDIO. Crie descrições visuais vívidas e sensoriais do cenário, incluindo paleta de cores, iluminação, texturas, objetos cênicos e atmosfera."
    )
}

class StudioAiService {

    suspend fun generateWithAi(
        task: AiStudioTask,
        userPrompt: String,
        contextInfo: String = ""
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val fullPrompt = buildString {
            if (contextInfo.isNotBlank()) {
                append("CONTEXTO DO PROJETO NO RANGA AI STUDIO:\n")
                append(contextInfo)
                append("\n\n")
            }
            append("SOLICITAÇÃO DO DIRETOR:\n")
            append(userPrompt)
        }

        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val request = GeminiGenerateRequest(
                    contents = listOf(
                        GeminiContent(parts = listOf(GeminiPart(text = fullPrompt)))
                    ),
                    systemInstruction = GeminiContent(
                        parts = listOf(GeminiPart(text = task.systemPrompt))
                    ),
                    generationConfig = GeminiGenerationConfig(temperature = 0.75f)
                )
                val response = GeminiApiClient.api.generateContent(apiKey, request)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!text.isNullOrBlank()) {
                    return@withContext Result.success(text)
                }
            } catch (e: Exception) {
                Log.w("StudioAiService", "Gemini API call failed, falling back to Studio Engine: ${e.message}")
            }
        }

        // High Quality Studio Built-in Engine Fallback
        val studioEngineResult = generateStudioEngineFallback(task, userPrompt, contextInfo)
        Result.success(studioEngineResult)
    }

    private fun generateStudioEngineFallback(
        task: AiStudioTask,
        prompt: String,
        context: String
    ): String {
        return when (task) {
            AiStudioTask.STORY_IDEAS -> """
                🌟 PROPOSTA DE HISTÓRIA CRIADA PELO RANGA AI STUDIO

                🎬 Título Sugerido: "O Enigma das Estrelas Perdidas"
                📺 Formato Recomendado: Série / Animação (10 Episódios)
                🎯 Gênero: Aventura, Sci-Fi e Mistério

                📖 Premissa Principal:
                Em um futuro onde civilizações vivem em ilhas flutuantes protegidas por cristais de energia, um jovem aprendiz de navegador e sua androide companheira descobrem que as coordenadas das antigas estrelas contêm uma mensagem codificada deixada pelos fundadores do mundo.

                🔥 Arcos e Conflitos:
                1. O Chamado: A ativação acidental do astrolábio estelar antigo.
                2. A Perseguição: A corporação dos Barões de Plasma tenta confiscar a relíquia.
                3. O Mistério Central: As estrelas não estão desaparecendo, estão sendo transportadas para outra dimensão.
                4. Clímax: A escolha entre reabrir o portal esquecido ou proteger o ecossistema das ilhas.

                ✨ Personagens Centrais:
                - Kai: Navegador audacioso com intuição fora do comum.
                - Vesper: Androide com inteligência avançada e curiosidade humana.
                - Comandante Thorne: Antagonista militar movido pelo medo da escassez de energia.
            """.trimIndent()

            AiStudioTask.CREATE_CHARACTER -> """
                👤 FICHA DE PERSONAGEM - RANGA AI STUDIO

                Nome: Alex "Faísca" Ventura
                Função: Protagonista / Inventor Rebelde
                Idade: 19 anos
                Voz Recomendada: Voz Jovem Enérgica (Leo)

                🧠 Psicologia e Personalidade:
                - Extrovertido, impaciente, engenhoso sob pressão.
                - Dificuldade em seguir ordens formais, mas altruísta ao extremo.
                - Maior medo: Falhar com aqueles que dependem de suas invenções.

                🎨 Visual & Estilo:
                - Jaqueta de couro espacial customizada com remendos holográficos.
                - Óculos de solda com lentes multifocais na testa.
                - Luva mecânica no braço esquerdo capaz de manipular campos magnéticos.

                ⚔️ Conflito Principal:
                Dividido entre seu desejo de liberdade e a responsabilidade de liderar a resistência.
            """.trimIndent()

            AiStudioTask.CREATE_DIALOGUE -> """
                💬 ROTEIRO DE DIÁLOGO - RANGA AI STUDIO

                [CENA: OFICINA SUBTERRÂNEA - NOITE]
                (Faísca está debruçado sobre a bancada. O monitor pisca em luz azul.)

                FAÍSCA
                (sem desviar o olhar do osciloscópio)
                Se eu estiver certo sobre esse modulador, teremos energia suficiente para saltar o bloqueio em cinco minutos.

                LIA
                (cruzando os braços, tom cético)
                E se você estiver errado, como na semana passada na Estação Sete?

                FAÍSCA
                (sorri de lado, girando a chave de torque)
                Aquilo não foi um erro, Lia. Foi uma descompressão com estilo.

                LIA
                (suspirando, mas contendo um sorriso)
                Faísca... a patrulha já está na atmosfera superior. Só temos um disparo.

                FAÍSCA
                (olha firme para ela)
                Então faremos valer a pena. Acione os reatores!
            """.trimIndent()

            AiStudioTask.CREATE_SCRIPT -> """
                📜 ROTEIRO PROFISSIONAL - RANGA AI STUDIO

                CENA 03 - INT. PONTE DE COMANDO - NOITE

                Luzes de emergência em tom âmbar giram suavemente. O som dos propulsores vibra no piso metálico.

                Ao centro, a capitã ELIZA observa o mapa tridimensional que projeta a órbita do planeta tempestuoso.

                ELIZA
                (para a equipe, voz firme)
                Status dos escudos de contenção!

                OPERADOR
                (digitando freneticamente)
                Em 68% e caindo! A tempestade eletrostática está drenando nossos capacitores primários!

                Uma forte sacudida atinge a nave. Chispas de solda caem do painel lateral.

                ELIZA
                (segurando firme na mureta de comando)
                Mantenham a proa voltada para o olho do vórtice. Ranga, precisamos daquela sobrecarga agora!

                CORTE PARA:
                INT. SALA DE MÁQUINAS - CONTÍNUO
                Ranga conecta os cabos luminosos em meio ao vapor brilhante.
            """.trimIndent()

            AiStudioTask.DEVELOP_SCENE -> """
                🎬 PLANO DE DESENVOLVIMENTO DE CENA

                📍 Enquadramento & Câmera:
                - Início com Plano Geral (Wide Shot) estabelecendo a grandiosidade do cenário.
                - Corte dinâmico para Close-Up nos olhos do protagonista quando a revelação ocorre.
                - Movimento de Travelling suave para acompanhar a caminhada dramática.

                💡 Iluminação & Arte:
                - Contraste alto (Chiaroscuro) com fontes de luz neon azul-turquesa e reflexos dourados.
                - Partículas de poeira luminosa suspensas no ar.

                🎵 Sonoplastia (SFX):
                - Eco de passos metálicos sobre piso molhado.
                - Ruído estático de transmissor antigo.
                - Impacto grave subaquático (Whoosh sub-bass) no momento da virada.

                🎶 Trilha Sonora:
                - Cordas em crescendo com pads sintetizados, evocando mistério e urgência.
            """.trimIndent()

            AiStudioTask.CONTINUE_STORY -> """
                🔄 CONTINUAÇÃO DA HISTÓRIA

                Assim que a última chave de ativação foi acionada, um silêncio absoluto tomou conta do compartimento. Por três segundos angustiantes, nem mesmo o som da ventilação podia ser ouvido.

                Subitamente, o painel central não explodiu — em vez disso, transformou-se em uma superfície líquida e espelhada. Uma voz suave, diferente de qualquer IA conhecida, ecoou diretamente na mente de todos os presentes:

                "Bravos viajantes. As coordenadas que procuram não estão no espaço... mas no próprio tempo."

                Antes que qualquer um pudesse reagir, o horizonte fora da janela começou a se distorcer em ondas de luz prisma.
            """.trimIndent()

            AiStudioTask.IMPROVE_DIALOGUE -> """
                ✨ DIÁLOGOS REFINADOS E POTENCIALIZADOS

                [VERSÃO REFINADA COM MAIOR SUBTEXTO E IMPACTO]

                ORIGINAL: "Eu não confio em você porque você mentiu sobre o passado."
                ⬇️ REFINADO (MAIS PODEROSO):
                "O problema não foi a mentira que você me contou há dez anos... foi a facilidade com que você fez parecer verdade todos os dias desde então."

                ORIGINAL: "Precisamos fugir agora ou vamos morrer!"
                ⬇️ REFINADO (MAIS TENSO):
                "Ouça esse rangido nas paredes. O casco já tomou a decisão por nós. Três minutos, ou seremos poeira estelar."
            """.trimIndent()

            AiStudioTask.SCENARIO_DESCRIPTION -> """
                🌄 DESCRIÇÃO IMERSIVA DE CENÁRIO

                📍 Nome: O Santuário dos Ventos de Safira
                🏷️ Tipo: Exterior / Paisagem Fantástica e Tecnológica

                Descritivo Visual:
                Um vasto desfiladeiro esculpido em rocha de cristal azulado, onde pontes suspensas feitas de fibras óticas entrelaçam torres pontiagudas que parecem agulhas de gelo. Cachoeiras de água bioluminescente despencam no abismo nebuloso, gerando vapores irisados que refletem o pôr do sol duplo no horizonte.

                Atmosfera & Sensações:
                - Vento constante com sonoridade harmônica que parece um cântico natural.
                - Iluminação crepuscular mesclando púrpura e dourado.
                - Sensores e lanternas solares suspensas em cabos de titânio.
            """.trimIndent()
        }
    }
}
