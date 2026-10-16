package com.example.data

import com.example.model.AlternativeQuest
import com.example.model.PetItem

object PetCatalog {

  val allItems: List<PetItem> = listOf(
    // Chapéus
    PetItem("capelo", "Capelo de Formatura", "hat", "🎓", "Símbolo dos grandes mestres do conhecimento.", "Item inicial liberado"),
    PetItem("bone", "Boné Gamer Esportivo", "hat", "🧢", "Estilo moderno para maratonas de estudo.", "Missão: Concluir 2 sessões de foco"),
    PetItem("coroa", "Coroa Dourada dos Campeões", "hat", "👑", "Para quem mantém a ofensiva firme e forte.", "Missão: Manter 2 dias de ofensiva"),
    PetItem("fones", "Fones Gamer Anti-Ruído", "hat", "🎧", "Isolamento acústico máximo contra distrações.", "Missão: Concluir 1 sessão de 45 ou 50 min"),
    PetItem("mago", "Chapéu Arcano da Sabedoria", "hat", "🧙", "Canaliza a energia mística de todas as fórmulas.", "Missão: Acertar 2 desafios no Mini-Quiz"),

    // Trajes
    PetItem("uniforme", "Uniforme Escolar Elegante", "outfit", "👔", "O clássico blazer verde bordado da raposinha.", "Item inicial liberado"),
    PetItem("kimono", "Quimono Ninja da Concentração", "outfit", "🥋", "Disciplina inabalável e foco absoluto.", "Missão: Fazer 1 pausa consciente"),
    PetItem("jaleco", "Jaleco de Cientista Brilhante", "outfit", "🥼", "Para experimentos e desvendar as leis do universo.", "Missão: Estudar uma matéria de Ciências/Exatas"),
    PetItem("gala", "Smoking de Gala Universitário", "outfit", "🤵", "Traje fino para as maiores cerimônias acadêmicas.", "Missão: Acumular 150 XP na jornada"),

    // Acessórios
    PetItem("oculos", "Óculos Redondos de Leitura", "accessory", "👓", "Clássicos e charmosos para ler centenas de páginas.", "Item inicial liberado"),
    PetItem("oculos_sol", "Óculos Escuros VIP", "accessory", "🕶️", "Mantenha o foco com muito estilo e confiança.", "Missão: Concluir 3 missões de estudo"),
    PetItem("folha", "Folha Zen da Floresta", "accessory", "🌿", "Conexão com a calma da natureza para evitar ansiedade.", "Missão: Alternar entre Fundamental e Médio")
  )

  fun getInitialQuests(): List<AlternativeQuest> = listOf(
    AlternativeQuest(
      id = "quest_bone",
      title = "Foco Veloz",
      description = "Conclua 2 sessões de estudo para destravar o Boné Gamer.",
      targetCount = 2,
      currentCount = 0,
      rewardItem = allItems.first { it.id == "bone" }
    ),
    AlternativeQuest(
      id = "quest_fones",
      title = "Mundo Silencioso",
      description = "Complete 1 sessão longa (45m ou 50m) para destravar os Fones Anti-Ruído.",
      targetCount = 1,
      currentCount = 0,
      rewardItem = allItems.first { it.id == "fones" }
    ),
    AlternativeQuest(
      id = "quest_mago",
      title = "Mestre dos Desafios",
      description = "Acerte 2 perguntas no Mini-Quiz para destravar o Chapéu de Mago.",
      targetCount = 2,
      currentCount = 0,
      rewardItem = allItems.first { it.id == "mago" }
    ),
    AlternativeQuest(
      id = "quest_jaleco",
      title = "Cientista em Ação",
      description = "Estude uma matéria científica para ganhar o Jaleco de Laboratório.",
      targetCount = 1,
      currentCount = 0,
      rewardItem = allItems.first { it.id == "jaleco" }
    ),
    AlternativeQuest(
      id = "quest_kimono",
      title = "Respiração Ninja",
      description = "Conclua 1 pausa de 5 minutos para destravar o Quimono da Concentração.",
      targetCount = 1,
      currentCount = 0,
      rewardItem = allItems.first { it.id == "kimono" }
    ),
    AlternativeQuest(
      id = "quest_coroa",
      title = "Rei da Ofensiva",
      description = "Mantenha 2 dias de estudo seguidos para ganhar a Coroa de Mestre.",
      targetCount = 2,
      currentCount = 1,
      rewardItem = allItems.first { it.id == "coroa" }
    ),
    AlternativeQuest(
      id = "quest_oculos_sol",
      title = "Estudante VIP",
      description = "Complete 3 ciclos de foco para destravar os Óculos Escuros.",
      targetCount = 3,
      currentCount = 0,
      rewardItem = allItems.first { it.id == "oculos_sol" }
    )
  )
}
