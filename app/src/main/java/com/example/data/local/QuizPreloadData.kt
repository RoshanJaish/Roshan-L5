package com.example.data.local

import com.example.data.model.*

object QuizPreloadData {

    val badgesList = listOf(
        BadgeItem(
            id = "super_star",
            name = "Super Star",
            description = "Achieved 100% score in any quiz with flying colors!",
            iconEmoji = "⭐",
            colorHex = 0xFFFFD043,
            isUnlocked = true,
            unlockCriteria = "Get a perfect score in any subject quiz"
        ),
        BadgeItem(
            id = "quiz_champion",
            name = "Quiz Champion",
            description = "Conquered the weekly Board Championship!",
            iconEmoji = "🏆",
            colorHex = 0xFF6C63FF,
            isUnlocked = true,
            unlockCriteria = "Participate and win a Top 3 in Championship"
        ),
        BadgeItem(
            id = "math_whiz",
            name = "Math Whiz Kid",
            description = "Solved 25 rapid math problems with accuracy!",
            iconEmoji = "🧮",
            colorHex = 0xFFFF6B6B,
            isUnlocked = true,
            unlockCriteria = "Complete 5 Mathematics quizzes"
        ),
        BadgeItem(
            id = "science_prodigy",
            name = "Science Prodigy",
            description = "Mastered Physics, Chemistry & Biology topics!",
            iconEmoji = "🔬",
            colorHex = 0xFF4EA8DE,
            isUnlocked = true,
            unlockCriteria = "Complete 5 Science quizzes"
        ),
        BadgeItem(
            id = "streak_legend",
            name = "Streak Legend",
            description = "Maintained a 7-day continuous study streak!",
            iconEmoji = "🔥",
            colorHex = 0xFFFF9F1C,
            isUnlocked = false,
            unlockCriteria = "Reach a 7-day quiz streak"
        ),
        BadgeItem(
            id = "speed_demon",
            name = "Speed Demon",
            description = "Finished a full 10-question test in under 60 seconds!",
            iconEmoji = "⚡",
            colorHex = 0xFF2EC4B6,
            isUnlocked = false,
            unlockCriteria = "Finish any quiz in under 1 minute with >80%"
        ),
        BadgeItem(
            id = "community_duelist",
            name = "Challenge Master",
            description = "Won 5 community topic challenges against classmates!",
            iconEmoji = "⚔️",
            colorHex = 0xFF9B5DE5,
            isUnlocked = false,
            unlockCriteria = "Win 5 1v1 Community Challenges"
        ),
        BadgeItem(
            id = "olympiad_grandmaster",
            name = "Grandmaster",
            description = "Reached Level 10 and 1000+ XP in academic rank!",
            iconEmoji = "👑",
            colorHex = 0xFFF15BB5,
            isUnlocked = false,
            unlockCriteria = "Accumulate 1000 XP"
        )
    )

    val friendsList = listOf(
        FriendItem(
            id = "f1",
            name = "Michael",
            username = "mike_champ",
            avatarColorHex = 0xFFFF7B54,
            streak = 16,
            points = 123,
            isOnline = true,
            board = "CBSE",
            grade = "Grade 8"
        ),
        FriendItem(
            id = "f2",
            name = "Jane",
            username = "jane_genius",
            avatarColorHex = 0xFF6BCB77,
            streak = 12,
            points = 1080,
            isOnline = true,
            board = "ICSE",
            grade = "Grade 8"
        ),
        FriendItem(
            id = "f3",
            name = "Amoora",
            username = "amoora_99",
            avatarColorHex = 0xFF4D96FF,
            streak = 7,
            points = 123,
            isOnline = false,
            board = "CBSE",
            grade = "Grade 8"
        ),
        FriendItem(
            id = "f4",
            name = "Alex Chen",
            username = "alex_c",
            avatarColorHex = 0xFFFFD93D,
            streak = 5,
            points = 95,
            isOnline = true,
            board = "Cambridge",
            grade = "Grade 7"
        ),
        FriendItem(
            id = "f5",
            name = "Zara Khan",
            username = "zara_k",
            avatarColorHex = 0xFFFF6B6B,
            streak = 9,
            points = 145,
            isOnline = false,
            board = "State Board",
            grade = "Grade 8"
        )
    )

    // Preloaded Quizzes
    val defaultQuizzes = listOf(
        Quiz(
            id = "math_apples_1",
            title = "Math Quiz",
            subtitle = "Practice your math skills!",
            category = SubjectCategory.MATH,
            board = BoardType.CBSE,
            grade = GradeLevel.GRADE_8,
            durationMinutes = 5,
            rewardXP = 50,
            difficulty = "Easy",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    text = "If Rani has 10 apples and gives 4 apples to her friend, how many apples are left?",
                    options = listOf("5 apples", "6 apples", "7 apples", "8 apples"),
                    correctOptionIndex = 1,
                    explanation = "10 - 4 = 6 apples remaining.",
                    points = 10
                ),
                QuizQuestion(
                    id = "q2",
                    text = "What is the square root of 144?",
                    options = listOf("10", "11", "12", "14"),
                    correctOptionIndex = 2,
                    explanation = "12 x 12 = 144, so the square root is 12.",
                    points = 10
                ),
                QuizQuestion(
                    id = "q3",
                    text = "Simplify the expression: 3x + 5 = 20. What is the value of x?",
                    options = listOf("3", "5", "15", "6"),
                    correctOptionIndex = 1,
                    explanation = "3x = 20 - 5 => 3x = 15 => x = 5.",
                    points = 10
                ),
                QuizQuestion(
                    id = "q4",
                    text = "The sum of the angles in any triangle is always equal to:",
                    options = listOf("90°", "180°", "270°", "360°"),
                    correctOptionIndex = 1,
                    explanation = "The interior angles of any triangle always add up to 180 degrees.",
                    points = 10
                ),
                QuizQuestion(
                    id = "q5",
                    text = "If a rectangle has length 8 cm and width 5 cm, what is its perimeter?",
                    options = listOf("40 cm", "26 cm", "13 cm", "30 cm"),
                    correctOptionIndex = 1,
                    explanation = "Perimeter = 2 * (length + width) = 2 * (8 + 5) = 26 cm.",
                    points = 10
                ),
                QuizQuestion(
                    id = "q6",
                    text = "What is 25% of 200?",
                    options = listOf("25", "40", "50", "75"),
                    correctOptionIndex = 2,
                    explanation = "25% of 200 = (25 / 100) * 200 = 50.",
                    points = 10
                ),
                QuizQuestion(
                    id = "q7",
                    text = "What is the next prime number after 13?",
                    options = listOf("15", "17", "19", "21"),
                    correctOptionIndex = 1,
                    explanation = "17 is prime as it has no factors other than 1 and 17.",
                    points = 10
                )
            )
        ),
        Quiz(
            id = "animals_nature_1",
            title = "Animals Name",
            subtitle = "Explore the wild world!",
            category = SubjectCategory.GENERAL_KNOWLEDGE,
            board = BoardType.CBSE,
            grade = GradeLevel.GRADE_8,
            durationMinutes = 6,
            rewardXP = 60,
            difficulty = "Medium",
            questions = listOf(
                QuizQuestion(
                    id = "an_1",
                    text = "Which mammal is known to have the most powerful bite force relative to size?",
                    options = listOf("Hippopotamus", "Lion", "Hyena", "Tasmanian Devil"),
                    correctOptionIndex = 0,
                    explanation = "Hippopotamus has one of the strongest bite forces among land mammals at over 12,000 Newtons.",
                    points = 10
                ),
                QuizQuestion(
                    id = "an_2",
                    text = "What is the only mammal capable of true sustained flight?",
                    options = listOf("Flying Squirrel", "Bat", "Sugar Glider", "Colugo"),
                    correctOptionIndex = 1,
                    explanation = "Bats are the only mammals capable of powered, flapping flight.",
                    points = 10
                ),
                QuizQuestion(
                    id = "an_3",
                    text = "Which animal breathes through its skin and lungs?",
                    options = listOf("Frog", "Lizard", "Crocodile", "Fish"),
                    correctOptionIndex = 0,
                    explanation = "Amphibians like frogs can perform cutaneous respiration through their moist skin.",
                    points = 10
                ),
                QuizQuestion(
                    id = "an_4",
                    text = "What color is a polar bear's skin under its white fur?",
                    options = listOf("White", "Pink", "Black", "Gray"),
                    correctOptionIndex = 2,
                    explanation = "Polar bear skin is black to absorb sunlight heat, while the fur is translucent.",
                    points = 10
                ),
                QuizQuestion(
                    id = "an_5",
                    text = "How many hearts does an octopus have?",
                    options = listOf("1", "2", "3", "4"),
                    correctOptionIndex = 2,
                    explanation = "An octopus has three hearts: two pump blood to the gills, one pumps through the body.",
                    points = 10
                )
            )
        ),
        Quiz(
            id = "science_physics_1",
            title = "Science Quest",
            subtitle = "Physics & Energy Masters",
            category = SubjectCategory.SCIENCE,
            board = BoardType.CBSE,
            grade = GradeLevel.GRADE_8,
            durationMinutes = 5,
            rewardXP = 55,
            difficulty = "Medium",
            questions = listOf(
                QuizQuestion(
                    id = "sci_1",
                    text = "What is the unit of electric current?",
                    options = listOf("Volt", "Ampere", "Ohm", "Watt"),
                    correctOptionIndex = 1,
                    explanation = "Ampere (A) is the SI unit of electric current.",
                    points = 10
                ),
                QuizQuestion(
                    id = "sci_2",
                    text = "Which gas do green plants absorb during photosynthesis?",
                    options = listOf("Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"),
                    correctOptionIndex = 1,
                    explanation = "Plants take in Carbon Dioxide (CO2) to manufacture glucose in sunlight.",
                    points = 10
                ),
                QuizQuestion(
                    id = "sci_3",
                    text = "What type of lens is used in a magnifying glass?",
                    options = listOf("Concave lens", "Convex lens", "Cylindrical lens", "Flat glass"),
                    correctOptionIndex = 1,
                    explanation = "A convex lens is a converging lens that produces a magnified virtual image.",
                    points = 10
                ),
                QuizQuestion(
                    id = "sci_4",
                    text = "What is the chemical formula for ordinary table salt?",
                    options = listOf("NaCl", "KCl", "CaCO3", "H2O"),
                    correctOptionIndex = 0,
                    explanation = "Sodium Chloride is NaCl.",
                    points = 10
                ),
                QuizQuestion(
                    id = "sci_5",
                    text = "Which planet is known as the 'Red Planet' in our solar system?",
                    options = listOf("Venus", "Mars", "Jupiter", "Mercury"),
                    correctOptionIndex = 1,
                    explanation = "Mars appears reddish due to iron oxide (rust) on its surface.",
                    points = 10
                )
            )
        ),
        Quiz(
            id = "english_mastery_1",
            title = "Grammar & Vocabulary",
            subtitle = "Sharpen your English skills",
            category = SubjectCategory.ENGLISH,
            board = BoardType.ICSE,
            grade = GradeLevel.GRADE_8,
            durationMinutes = 5,
            rewardXP = 45,
            difficulty = "Easy",
            questions = listOf(
                QuizQuestion(
                    id = "eng_1",
                    text = "Identify the adverb in the sentence: 'The curious kitten walked stealthily across the floor.'",
                    options = listOf("curious", "walked", "stealthily", "floor"),
                    correctOptionIndex = 2,
                    explanation = "'Stealthily' modifies the verb 'walked', describing how it walked.",
                    points = 10
                ),
                QuizQuestion(
                    id = "eng_2",
                    text = "What is the synonym of the word 'Abundant'?",
                    options = listOf("Scarce", "Plentiful", "Tiny", "Empty"),
                    correctOptionIndex = 1,
                    explanation = "Abundant means existing or available in large quantities (plentiful).",
                    points = 10
                ),
                QuizQuestion(
                    id = "eng_3",
                    text = "Choose the correct spelling:",
                    options = listOf("Accomodate", "Acommodate", "Accommodate", "Acomodate"),
                    correctOptionIndex = 2,
                    explanation = "Accommodate has double 'c' and double 'm'.",
                    points = 10
                ),
                QuizQuestion(
                    id = "eng_4",
                    text = "What figure of speech is used in: 'The wind whispered through the dark trees'?",
                    options = listOf("Simile", "Personification", "Hyperbole", "Metaphor"),
                    correctOptionIndex = 1,
                    explanation = "Giving human traits (whispering) to non-human things (wind) is personification.",
                    points = 10
                )
            )
        ),
        Quiz(
            id = "tech_coding_1",
            title = "Computer & Logic",
            subtitle = "Binary, logic & coding foundations",
            category = SubjectCategory.TECH,
            board = BoardType.CBSE,
            grade = GradeLevel.GRADE_8,
            durationMinutes = 5,
            rewardXP = 50,
            difficulty = "Medium",
            questions = listOf(
                QuizQuestion(
                    id = "tech_1",
                    text = "How many bits are there in a single byte?",
                    options = listOf("4 bits", "8 bits", "16 bits", "32 bits"),
                    correctOptionIndex = 1,
                    explanation = "1 Byte is composed of 8 binary digits (bits).",
                    points = 10
                ),
                QuizQuestion(
                    id = "tech_2",
                    text = "Which of the following is considered the 'Brain' of a computer?",
                    options = listOf("Hard Disk", "CPU (Central Processing Unit)", "RAM", "Monitor"),
                    correctOptionIndex = 1,
                    explanation = "The CPU executes computer program instructions and calculations.",
                    points = 10
                ),
                QuizQuestion(
                    id = "tech_3",
                    text = "What does HTTP stand for in web browsing?",
                    options = listOf("HyperText Transfer Protocol", "High Thermal Processing", "Home Tool Transfer Path", "Hyper Terminal Text Port"),
                    correctOptionIndex = 0,
                    explanation = "HTTP stands for HyperText Transfer Protocol.",
                    points = 10
                )
            )
        ),
        Quiz(
            id = "social_studies_1",
            title = "History & Civics",
            subtitle = "Civilizations & Constitution",
            category = SubjectCategory.SOCIAL_STUDIES,
            board = BoardType.STATE_BOARD,
            grade = GradeLevel.GRADE_8,
            durationMinutes = 5,
            rewardXP = 50,
            difficulty = "Medium",
            questions = listOf(
                QuizQuestion(
                    id = "soc_1",
                    text = "Who is known as the 'Father of the Indian Constitution'?",
                    options = listOf("Mahatma Gandhi", "Dr. B.R. Ambedkar", "Jawaharlal Nehru", "Sardar Patel"),
                    correctOptionIndex = 1,
                    explanation = "Dr. Bhimrao Ramji Ambedkar served as the chairman of the drafting committee.",
                    points = 10
                ),
                QuizQuestion(
                    id = "soc_2",
                    text = "Which ancient river valley civilization is famous for the city of Mohenjo-Daro?",
                    options = listOf("Mesopotamian", "Indus Valley", "Egyptian", "Chinese"),
                    correctOptionIndex = 1,
                    explanation = "Mohenjo-Daro and Harappa are major cities of the Indus Valley Civilization.",
                    points = 10
                ),
                QuizQuestion(
                    id = "soc_3",
                    text = "Which layer of Earth's atmosphere contains the protective ozone layer?",
                    options = listOf("Troposphere", "Stratosphere", "Mesosphere", "Thermosphere"),
                    correctOptionIndex = 1,
                    explanation = "The stratosphere holds the ozone layer which filters solar ultraviolet radiation.",
                    points = 10
                )
            )
        )
    )

    val championshipEvents = listOf(
        ChampionshipEvent(
            id = "champ_national_1",
            title = "All-India Board Championship 🏆",
            subtitle = "CBSE & ICSE Grade 6-10 Grand Olympiad",
            board = BoardType.CBSE,
            gradeGroup = "Grade 6-10",
            subject = SubjectCategory.MATH,
            prizeXP = 500,
            participantsCount = 1420,
            status = "Live Now",
            endsInMinutes = 45,
            quiz = Quiz(
                id = "champ_quiz_1",
                title = "National Math Championship",
                subtitle = "Grand Inter-School Tournament",
                category = SubjectCategory.MATH,
                board = BoardType.CBSE,
                grade = GradeLevel.GRADE_8,
                durationMinutes = 8,
                rewardXP = 500,
                isChampionship = true,
                difficulty = "Hard",
                questions = listOf(
                    QuizQuestion(
                        id = "cq_1",
                        text = "A train 120m long travels at 72 km/h. How many seconds will it take to cross a pole?",
                        options = listOf("4 seconds", "6 seconds", "8 seconds", "10 seconds"),
                        correctOptionIndex = 1,
                        explanation = "Speed in m/s = 72 * (5/18) = 20 m/s. Time = Distance / Speed = 120 / 20 = 6 seconds.",
                        points = 25
                    ),
                    QuizQuestion(
                        id = "cq_2",
                        text = "If a polygon has 6 sides, what is the sum of its interior angles?",
                        options = listOf("540°", "720°", "900°", "1080°"),
                        correctOptionIndex = 1,
                        explanation = "Formula = (n - 2) * 180° = (6 - 2) * 180° = 4 * 180° = 720°.",
                        points = 25
                    ),
                    QuizQuestion(
                        id = "cq_3",
                        text = "In a class of 40 students, 60% are girls. How many boys are in the class?",
                        options = listOf("12 boys", "16 boys", "20 boys", "24 boys"),
                        correctOptionIndex = 1,
                        explanation = "Girls = 60% of 40 = 24. Boys = 40 - 24 = 16 boys.",
                        points = 25
                    ),
                    QuizQuestion(
                        id = "cq_4",
                        text = "What is the greatest common divisor (GCD) of 36 and 84?",
                        options = listOf("6", "12", "18", "24"),
                        correctOptionIndex = 1,
                        explanation = "36 = 12 * 3, 84 = 12 * 7. GCD is 12.",
                        points = 25
                    )
                )
            )
        ),
        ChampionshipEvent(
            id = "champ_science_cup",
            title = "Junior Science Olympiad Cup 🔬",
            subtitle = "Physics, Chemistry & Discovery Showdown",
            board = BoardType.ICSE,
            gradeGroup = "Grade 7-9",
            subject = SubjectCategory.SCIENCE,
            prizeXP = 350,
            participantsCount = 890,
            status = "Live Now",
            endsInMinutes = 120,
            quiz = defaultQuizzes[2].copy(isChampionship = true, rewardXP = 350)
        )
    )

    val communityChallenges = listOf(
        CommunityChallenge(
            id = "comm_1",
            creatorName = "Michael",
            creatorAvatarColor = 0xFFFF7B54,
            title = "Michael's Math Showdown ⚔️",
            topic = "Fractions, Ratios & Linear Equations",
            board = BoardType.CBSE,
            grade = GradeLevel.GRADE_8,
            subject = SubjectCategory.MATH,
            prizeXP = 120,
            participantsCount = 28,
            maxParticipants = 50,
            quiz = defaultQuizzes[0].copy(title = "Michael's Math Showdown", isCommunityChallenge = true)
        ),
        CommunityChallenge(
            id = "comm_2",
            creatorName = "Jane",
            creatorAvatarColor = 0xFF6BCB77,
            title = "Jane's Science Speedrun ⚡",
            topic = "Light, Reflection & Cell Biology",
            board = BoardType.ICSE,
            grade = GradeLevel.GRADE_8,
            subject = SubjectCategory.SCIENCE,
            prizeXP = 150,
            participantsCount = 42,
            maxParticipants = 60,
            quiz = defaultQuizzes[2].copy(title = "Jane's Science Speedrun", isCommunityChallenge = true)
        ),
        CommunityChallenge(
            id = "comm_3",
            creatorName = "Alex Chen",
            creatorAvatarColor = 0xFFFFD93D,
            title = "Grammar Duel: Idioms & Clauses 📖",
            topic = "Advanced sentence structures & vocabulary",
            board = BoardType.CAMBRIDGE,
            grade = GradeLevel.GRADE_8,
            subject = SubjectCategory.ENGLISH,
            prizeXP = 100,
            participantsCount = 19,
            maxParticipants = 30,
            quiz = defaultQuizzes[3].copy(title = "Grammar Duel", isCommunityChallenge = true)
        )
    )

    val categoryEntities = listOf(
        QuizCategoryEntity("MATH", "Math", "Arithmetic, Algebra & Geometry", "math", 0xFFFF6B6B),
        QuizCategoryEntity("SCIENCE", "Science", "Physics, Chemistry & Biology", "science", 0xFF4EA8DE),
        QuizCategoryEntity("ENGLISH", "English", "Grammar, Vocabulary & Comprehension", "english", 0xFF2EC4B6),
        QuizCategoryEntity("SOCIAL_STUDIES", "Social", "History, Geography & Civics", "history", 0xFFFF9F1C),
        QuizCategoryEntity("TECH", "Computer", "Coding, Logic & Cyber Skills", "tech", 0xFF9B5DE5),
        QuizCategoryEntity("GENERAL_KNOWLEDGE", "GK & Animals", "Wildlife, World Records & General Knowledge", "gk", 0xFFFFD043)
    )

    fun quizToEntity(quiz: Quiz): QuizEntity {
        return QuizEntity(
            id = quiz.id,
            title = quiz.title,
            subtitle = quiz.subtitle,
            category = quiz.category.name,
            board = quiz.board.name,
            grade = quiz.grade.name,
            durationMinutes = quiz.durationMinutes,
            isChampionship = quiz.isChampionship,
            isCommunityChallenge = quiz.isCommunityChallenge,
            rewardXP = quiz.rewardXP,
            difficulty = quiz.difficulty,
            questionCount = quiz.questions.size
        )
    }

    fun questionToEntity(q: QuizQuestion, quizId: String, category: String, board: String, grade: String): QuizQuestionEntity {
        return QuizQuestionEntity(
            id = q.id,
            quizId = quizId,
            text = q.text,
            optionsJson = q.options.joinToString("||"),
            correctOptionIndex = q.correctOptionIndex,
            explanation = q.explanation,
            points = q.points,
            category = category,
            board = board,
            grade = grade
        )
    }

    fun entityToQuestion(entity: QuizQuestionEntity): QuizQuestion {
        return QuizQuestion(
            id = entity.id,
            text = entity.text,
            options = entity.optionsJson.split("||"),
            correctOptionIndex = entity.correctOptionIndex,
            explanation = entity.explanation,
            points = entity.points
        )
    }

    fun entityToQuiz(entity: QuizEntity, questions: List<QuizQuestion>): Quiz {
        val cat = try { SubjectCategory.valueOf(entity.category) } catch (e: Exception) { SubjectCategory.ALL }
        val board = try { BoardType.valueOf(entity.board) } catch (e: Exception) { BoardType.CBSE }
        val grade = try { GradeLevel.valueOf(entity.grade) } catch (e: Exception) { GradeLevel.GRADE_8 }
        return Quiz(
            id = entity.id,
            title = entity.title,
            subtitle = entity.subtitle,
            category = cat,
            board = board,
            grade = grade,
            questions = questions,
            durationMinutes = entity.durationMinutes,
            isChampionship = entity.isChampionship,
            isCommunityChallenge = entity.isCommunityChallenge,
            rewardXP = entity.rewardXP,
            difficulty = entity.difficulty
        )
    }
}

