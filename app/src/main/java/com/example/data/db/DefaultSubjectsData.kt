package com.example.data.db

import com.example.model.EducationLevel
import com.example.model.QuizQuestion
import com.example.model.StudySubject

object DefaultSubjectsData {

  fun getDefaultSubjects(): List<StudySubject> = listOf(
    // ====== ENSINO FUNDAMENTAL ======
    StudySubject(
      id = "fund_matematica",
      name = "Matemática Básica",
      educationLevel = EducationLevel.FUNDAMENTAL,
      category = "Exatas",
      colorHex = "#10B981",
      iconKey = "calculate",
      shortTip = "Domine a base! Resolva frações, regra de três e equações simples com calma e clareza nos passos.",
      topics = listOf(
        "Frações e Operações Básicas",
        "Regra de Três Simples e Composta",
        "Porcentagem e Juros Simples",
        "Equações e Inequações do 1º Grau",
        "Geometria Básica (Áreas e Perímetros)",
        "Potenciação e Raízes Quadradas"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_fund_mat_1",
          question = "Se uma blusa de R$ 80,00 tem 25% de desconto, qual será o valor final a pagar?",
          options = listOf("R$ 60,00", "R$ 65,00", "R$ 55,00", "R$ 70,00"),
          correctOptionIndex = 0,
          explanation = "25% de 80 é (80 × 0,25) = 20. Subtraindo do valor: 80 - 20 = R$ 60,00."
        ),
        QuizQuestion(
          id = "q_fund_mat_2",
          question = "Qual é o valor de x na equação 2x + 6 = 20?",
          options = listOf("x = 7", "x = 8", "x = 6", "x = 10"),
          correctOptionIndex = 0,
          explanation = "2x = 20 - 6 -> 2x = 14 -> x = 7."
        )
      ),
      priorityWeight = 3
    ),

    StudySubject(
      id = "fund_portugues",
      name = "Língua Portuguesa & Gramática",
      educationLevel = EducationLevel.FUNDAMENTAL,
      category = "Linguagens",
      colorHex = "#059669",
      iconKey = "menu_book",
      shortTip = "A leitura atenta do texto e o conhecimento das classes de palavras são a chave para gabaritar interpretação.",
      topics = listOf(
        "Classes Gramaticais (Substantivo, Verbo, Adjetivo)",
        "Concordância Verbal e Nominal",
        "Interpretação e Compreensão de Texto",
        "Uso dos Porquês e Ortografia",
        "Pontuação (Vírgula, Ponto e Vírgula)",
        "Figuras de Linguagem Básicas"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_fund_port_1",
          question = "Assinale a opção correta para a lacuna: 'Fiquei feliz _____ tirei nota dez!'",
          options = listOf("porque", "por que", "por quê", "porquê"),
          correctOptionIndex = 0,
          explanation = "'Porque' junto e sem acento funciona como conjunção explicativa ou causal."
        )
      ),
      priorityWeight = 3
    ),

    StudySubject(
      id = "fund_ciencias",
      name = "Ciências da Natureza",
      educationLevel = EducationLevel.FUNDAMENTAL,
      category = "Biológicas",
      colorHex = "#14B8A6",
      iconKey = "science",
      shortTip = "Observe os fenômenos do dia a dia: estados da matéria, respiração, fotossíntese e corpo humano.",
      topics = listOf(
        "A Célula: Unidade Fundamental da Vida",
        "Corpo Humano (Digestão, Respiração, Circulação)",
        "Cadeias e Teias Alimentares",
        "Estados Físicos da Matéria e Ciclo da Água",
        "Sistema Solar e Movimentos da Terra"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_fund_cie_1",
          question = "Qual organela celular é responsável pela produção de energia (ATP) por meio da respiração celular?",
          options = listOf("Mitocôndria", "Ribossomo", "Lisossomo", "Centríolo"),
          correctOptionIndex = 0,
          explanation = "As mitocôndrias produzem ATP através da respiração aeróbia celular."
        )
      ),
      priorityWeight = 2
    ),

    StudySubject(
      id = "fund_historia",
      name = "História Geral & do Brasil",
      educationLevel = EducationLevel.FUNDAMENTAL,
      category = "Humanas",
      colorHex = "#0D9488",
      iconKey = "history_edu",
      shortTip = "Conecte os períodos históricos em uma linha do tempo, sempre compreendendo causas sociais e econômicas.",
      topics = listOf(
        "Brasil Colônia e Primeiros Ciclos",
        "Independência do Brasil e Primeiro Reinado",
        "Civilizações da Antiguidade (Grécia e Roma)",
        "Feudalismo e Idade Média",
        "Revolução Industrial e Sociedade Moderna"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_fund_hist_1",
          question = "Em que ano foi proclamada a Independência do Brasil por Dom Pedro I?",
          options = listOf("1822", "1889", "1500", "1808"),
          correctOptionIndex = 0,
          explanation = "A Independência do Brasil foi declarada em 7 de setembro de 1822."
        )
      ),
      priorityWeight = 2
    ),

    StudySubject(
      id = "fund_geografia",
      name = "Geografia Geral",
      educationLevel = EducationLevel.FUNDAMENTAL,
      category = "Humanas",
      colorHex = "#0284C7",
      iconKey = "public",
      shortTip = "Explore mapas e compreenda a relação entre o relevo, clima e as transformações feitas pelas cidades.",
      topics = listOf(
        "Cartografia, Escalas e Coordenadas Geográficas",
        "Relevo, Hidrografia e Clima",
        "Os 6 Biomas Brasileiros",
        "Urbanização e População Brasileira",
        "Fuso Horário e Movimentos Planetários"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_fund_geo_1",
          question = "Qual é o bioma exclusivamente brasileiro caracterizado por clima semiárido e vegetação adaptada à seca?",
          options = listOf("Caatinga", "Cerrado", "Pampa", "Pantanal"),
          correctOptionIndex = 0,
          explanation = "A Caatinga é um bioma exclusivamente brasileiro com plantas xerófilas."
        )
      ),
      priorityWeight = 2
    ),

    // ====== ENSINO MÉDIO ======
    StudySubject(
      id = "med_fisica",
      name = "Física (Médio)",
      educationLevel = EducationLevel.MEDIO,
      category = "Exatas",
      colorHex = "#F59E0B",
      iconKey = "bolt",
      shortTip = "Isole os dados no Sistema Internacional (m, s, kg) e desenhe diagramas de forças antes de calcular.",
      topics = listOf(
        "Cinemática Escalar (MRU e MRUV)",
        "Dinâmica: As 3 Leis de Newton",
        "Trabalho, Potência e Conservação de Energia",
        "Termologia, Calorimetria e Dilatação",
        "Ondulatória e Acústica",
        "Eletrodinâmica: Circuitos e Lei de Ohm"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_med_fis_1",
          question = "Pela 1ª Lei de Newton, o que acontece com um corpo livre da ação de forças resultantes?",
          options = listOf(
            "Permanece em repouso ou em movimento retilíneo uniforme",
            "Para imediatamente",
            "Aumenta sua velocidade progressivamente",
            "Muda constantemente de trajetória"
          ),
          correctOptionIndex = 0,
          explanation = "O princípio da inércia garante a manutenção da velocidade vetorial constante quando Fr = 0."
        ),
        QuizQuestion(
          id = "q_med_fis_2",
          question = "Qual fórmula representa a 1ª Lei de Ohm na eletrodinâmica?",
          options = listOf("U = R · i", "P = i² / R", "F = m · a", "v = v₀ + a · t"),
          correctOptionIndex = 0,
          explanation = "A diferença de potencial elétrico U é igual à resistência R multiplicada pela corrente i."
        )
      ),
      priorityWeight = 3
    ),

    StudySubject(
      id = "med_quimica",
      name = "Química (Médio)",
      educationLevel = EducationLevel.MEDIO,
      category = "Exatas",
      colorHex = "#EA580C",
      iconKey = "biotech",
      shortTip = "Balancear a reação é obrigatório! Atenção especial à estequiometria, molaridade e química orgânica.",
      topics = listOf(
        "Modelos Atômicos e Distribuição Eletrônica",
        "Tabela Periódica e Ligações Químicas",
        "Cálculo Estequiométrico e Mol",
        "Soluções, Concentração e Solubilidade",
        "Termoquímica e Cinética Química",
        "Química Orgânica (Hidrocarbonetos e Funções Oxigenadas)"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_med_quim_1",
          question = "Qual ligação química é formada pelo compartilhamento de elétrons entre ametais?",
          options = listOf("Ligação Covalente", "Ligação Iônica", "Ligação Metálica", "Força de London"),
          correctOptionIndex = 0,
          explanation = "A ligação covalente ocorre pelo compartilhamento mútuo de pares eletrônicos."
        )
      ),
      priorityWeight = 3
    ),

    StudySubject(
      id = "med_biologia",
      name = "Biologia (Médio)",
      educationLevel = EducationLevel.MEDIO,
      category = "Biológicas",
      colorHex = "#D97706",
      iconKey = "psychology",
      shortTip = "Foques em Genética, Citologia e Ecologia — os três temas mais recorrentes no ENEM e vestibulares.",
      topics = listOf(
        "Genética: 1ª e 2ª Leis de Mendel e Heredogramas",
        "Estrutura Celular, Membrana e Transporte",
        "Metabolismo Energético (Fotossíntese vs Respiração)",
        "Fisiologia Humana (Cardiovascular, Imune e Nervoso)",
        "Evolução Biológica (Darwinismo e Neodarwinismo)",
        "Ecologia: Ciclos Biogeoquímicos e Desequilíbrios Ambientais"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_med_bio_1",
          question = "Na molécula de DNA, qual base nitrogenada pareia especificamente com a Timina (T)?",
          options = listOf("Adenina (A)", "Guanina (G)", "Citosina (C)", "Uracila (U)"),
          correctOptionIndex = 0,
          explanation = "No DNA: Adenina liga com Timina (duas pontes de H) e Citosina liga com Guanina."
        )
      ),
      priorityWeight = 3
    ),

    StudySubject(
      id = "med_matematica",
      name = "Matemática Avançada (Médio)",
      educationLevel = EducationLevel.MEDIO,
      category = "Exatas",
      colorHex = "#F97316",
      iconKey = "functions",
      shortTip = "Domine gráficos de funções (afim e quadrática) e análise combinatória. São questões certas!",
      topics = listOf(
        "Função Afim e Quadrática (Gráficos e Vértices)",
        "Funções Exponenciais e Logarítmicas",
        "Trigonometria no Triângulo e Círculo Trigonométrico",
        "Análise Combinatória (Arranjos, Combinações e Permutações)",
        "Probabilidade Simples e Condicional",
        "Geometria Espacial (Prismas, Cilindros, Pirâmides e Esferas)"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_med_mat_1",
          question = "Quantos anagramas podem ser formados com as letras da palavra 'ESTUDO' (sem repetições)?",
          options = listOf("720", "120", "240", "360"),
          correctOptionIndex = 0,
          explanation = "Como são 6 letras distintas, usamos permutação simples: P6 = 6! = 6 × 5 × 4 × 3 × 2 × 1 = 720."
        )
      ),
      priorityWeight = 3
    ),

    StudySubject(
      id = "med_redacao",
      name = "Redação Dissertativa (ENEM)",
      educationLevel = EducationLevel.MEDIO,
      category = "Linguagens",
      colorHex = "#C2410C",
      iconKey = "edit_note",
      shortTip = "Não esqueça os 5 elementos da intervenção: Agente, Ação, Meio/Modo, Efeito e Detalhamento.",
      topics = listOf(
        "Estrutura do Texto Dissertativo-Argumentativo",
        "Tese Clara e Projeto de Texto",
        "Repertório Sociocultural Legitimado e Produtivo",
        "Coesão Textual e Operadores Argumentativos",
        "Proposta de Intervenção Completa com 5 Elementos",
        "Análise de Temas Sociais e Históricos Contemporâneos"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_med_red_1",
          question = "Qual elemento abaixo é indispensável para a nota máxima na Competência 5 do ENEM?",
          options = listOf(
            "Proposta de Intervenção com os 5 elementos detalhados",
            "Uso de vocabulário arcaico",
            "Mudar a opinião do corretor",
            "Terminar o texto com perguntas retóricas"
          ),
          correctOptionIndex = 0,
          explanation = "A Competência 5 exige agente, ação, meio, finalidade e pelo menos um detalhamento."
        )
      ),
      priorityWeight = 3
    ),

    StudySubject(
      id = "med_filosofia_sociologia",
      name = "Filosofia & Sociologia",
      educationLevel = EducationLevel.MEDIO,
      category = "Humanas",
      colorHex = "#B45309",
      iconKey = "menu_book",
      shortTip = "Conecte os autores clássicos aos dilemas modernos: cidadania, tecnologia e ética na sociedade.",
      topics = listOf(
        "Filosofia Grega: Sócrates, Platão e Aristóteles",
        "Os Filósofos Contratualistas (Hobbes, Locke e Rousseau)",
        "Ética, Moral e Teoria do Conhecimento",
        "Os Clássicos da Sociologia: Durkheim, Weber e Marx",
        "Indústria Cultural e Sociedade de Consumo",
        "Movimentos Sociais e Direitos Humanos"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_med_fil_1",
          question = "Qual pensador é reconhecido pelo conceito do 'Mito da Caverna' para debater o mundo das ideias?",
          options = listOf("Platão", "Sócrates", "Aristóteles", "René Descartes"),
          correctOptionIndex = 0,
          explanation = "Platão apresenta o Mito da Caverna em sua obra 'A República'."
        )
      ),
      priorityWeight = 2
    ),

    StudySubject(
      id = "med_literatura",
      name = "Literatura Brasileira",
      educationLevel = EducationLevel.MEDIO,
      category = "Linguagens",
      colorHex = "#9A3412",
      iconKey = "auto_stories",
      shortTip = "Associe as escolas literárias aos seus contextos: o Realismo criticava as ilusões românticas burguesas.",
      topics = listOf(
        "Barroco e Arcadismo no Brasil",
        "Romantismo: Poesia e Romance",
        "Realismo e Naturalismo (Machado de Assis)",
        "Modernismo de 1922: Semana de Arte Moderna",
        "Geração de 30: Drummond, Graciliano Ramos e Cecília Meireles",
        "Literatura Contemporânea e Poesia Marginal"
      ),
      questions = listOf(
        QuizQuestion(
          id = "q_med_lit_1",
          question = "Qual célebre romance machadiano iniciou o movimento do Realismo no Brasil em 1881?",
          options = listOf(
            "Memórias Póstumas de Brás Cubas",
            "Dom Casmurro",
            "O Guarani",
            "O Cortiço"
          ),
          correctOptionIndex = 0,
          explanation = "'Memórias Póstumas de Brás Cubas' inaugurou o Realismo brasileiro com sua ironia fina e narrador defunto."
        )
      ),
      priorityWeight = 2
    )
  )
}
