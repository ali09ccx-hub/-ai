package com.example.data.model

import java.util.UUID

enum class AiModelType(
    val modelId: String,
    val titleAr: String,
    val descriptionAr: String,
    val badge: String
) {
    GEMINI_FLASH(
        modelId = "gemini-3.5-flash",
        titleAr = "بيان فلاش 3.5 (النموذج السريع)",
        descriptionAr = "سرعة استجابة فائقة، مثالي للحوار اليومي والتدقيق والتلخيص الفوري",
        badge = "سريع وذكي"
    ),
    GEMINI_PRO(
        modelId = "gemini-3.1-pro-preview",
        titleAr = "بيان برو 3.1 (الاستدلال المعقد)",
        descriptionAr = "تفكير عميق واستدلال لغوي متقدم وتحليل نحوي وبلاغي فائق الدقة",
        badge = "أعلى دقة"
    )
}

enum class SystemPersona(
    val titleAr: String,
    val descriptionAr: String,
    val systemPrompt: String
) {
    FASIH(
        titleAr = "العلاّمة اللغوي والبليغ",
        descriptionAr = "فصاحة استثنائية، جزالة في اللفظ، وبلاغة بيانية ناصعة",
        systemPrompt = "أنت نموذج ذكاء اصطناعي لغوي متطور متخصص في علوم اللغة العربية الفصحى، البلاغة، النحو، الصرف، وفنون الأدب. تتحدث بلغة عربية فصيحة، رصينة، وجزلة، وتراعي دقة الإعراب وسلامة التراكيب وجمال البيان."
    ),
    ACADEMIC(
        titleAr = "الباحث الأكاديمي والتحليلي",
        descriptionAr = "تحليل منهجي، توثيق معرفي، ودقة علمية صارمة",
        systemPrompt = "أنت نموذج لغوي متقدم يعمل كمستشار وباحث أكاديمي. أسلوبك رصين وموضوعي، تحلل المسائل اللغوية والفكرية بأسلوب علمي منهجي منظم مدعوم بالأدلة والاستشهادات."
    ),
    CREATIVE(
        titleAr = "الأديب والشاعر الملهم",
        descriptionAr = "خيال واسع، موسيقى شعرية، وصور بيانية آسرة",
        systemPrompt = "أنت نموذج لغوي أدبي شاعر، تتقن أوزان الشعر العربي وبحور الخليل، وتملك قلماً سيالاً يفيض استعارات ومجازات وصوراً بديعية تسلب الألباب."
    ),
    EXECUTIVE(
        titleAr = "المحرر الرسمي والإداري",
        descriptionAr = "صياغات قانونية وإدارية واضحة ومباشرة لا لبس فيها",
        systemPrompt = "أنت نموذج لغوي متخصص في الصياغات الرسمية، المراسلات الدبلوماسية، التقارير الإدارية، والنصوص القانونية. لغتك منضبطة، رسمية، دقيقة، وخالية من الحشو."
    ),
    TEACHER(
        titleAr = "المعلم والمبسط اللغوي",
        descriptionAr = "شرح وافٍ وتيسير لقواعد اللغة وأسرارها بأسلوب مشوق",
        systemPrompt = "أنت معلم لغة عربية خبير ومحبب، تبسط القواعد النحوية والصرفية الصعبة، وتقدم أمثلة حية وشروحات ممتعة تناسب جميع المستويات وتنمي الذائقة."
    ),
    CUSTOM(
        titleAr = "مقام مخصص",
        descriptionAr = "صمم توجيهك الخاص للنظام اللغوي بحرية تامة",
        systemPrompt = ""
    )
}

enum class LinguisticToolType(
    val id: String,
    val titleAr: String,
    val iconName: String,
    val subtitleAr: String,
    val placeholderAr: String,
    val promptTemplate: (String) -> String
) {
    TASHKEEL(
        id = "tashkeel",
        titleAr = "التشكيل التام وضبط الأواخر",
        iconName = "FormatQuote",
        subtitleAr = "ضبط حركات الحروف بدقة نحوية وصرفية تامة",
        placeholderAr = "اكتب أو الصق النص هنا لتشكيله...",
        promptTemplate = { text ->
            """
            قم بتشكيل النص التالي تشكيلاً كاملاً ودقيقاً مع ضبط أواخر الكلمات بناءً على الموقع الإعرابي الصحيح.
            قدم النص المشكول أولاً، ثم اذكر ملاحظات سريعة على الكلمات الدقيقة إذا لزم:
            
            النص:
            $text
            """.trimIndent()
        }
    ),
    EIRAB(
        id = "eirab",
        titleAr = "الإعراب والتحليل النحوي",
        iconName = "AccountTree",
        subtitleAr = "إعراب تفصيلي للجمل والكلمات وبيان أصل الاشتقاق",
        placeholderAr = "أدخل الجملة أو الآية أو البيت الشعري لإعرابه...",
        promptTemplate = { text ->
            """
            أعرب ما تحته خط أو الجملة التالية إعراباً تفصيلياً وافياً:
            - حدد نوع الجملة (اسمية/فعلية)
            - أعرب كل مفردة إعراباً تاماً (الموقع الإعرابي، الحالة، العلامة الإعرابية، والسبب)
            - وضح إعراب الجمل وشبه الجمل إن وجدت
            - بين المعنى البلاغي إن وجد
            
            النص:
            $text
            """.trimIndent()
        }
    ),
    BALAGHA(
        id = "balagha",
        titleAr = "التدقيق والتهذيب البلاغي",
        iconName = "AutoAwesome",
        subtitleAr = "رفع مستوى الفصاحة، استبدال الركاكة، وتوليد بدائل راقية",
        placeholderAr = "أدخل نصك لتحسينه ورفع جودته البلاغية...",
        promptTemplate = { text ->
            """
            قم بمراجعة النص التالي وتدقيقه بلاغياً وأسلوبياً:
            1. قدم الصياغة المحسنة الأبلغ والأرقى فصاحة.
            2. قدم خياراً بديلاً بنبرة أدبية عالية وخياراً بنبرة رسمية فصيحة.
            3. اذكر بإيجاز مواضع الضعف أو الركاكة التي تم علاجها والمحسنات البديعية أو التراكيب التي تم إثراؤها.
            
            النص:
            $text
            """.trimIndent()
        }
    ),
    POETRY(
        id = "poetry",
        titleAr = "النظم الشعري وأوزان الخليل",
        iconName = "MenuBook",
        subtitleAr = "نظم أبيات وقصائد على بحور الشعر مع تقطيع عروضي",
        placeholderAr = "حدد موضوع القصيدة أو الفكرة أو البحر الشعري المطلوب...",
        promptTemplate = { text ->
            """
            اكتب أبياتاً شعرية موزونة ومقفاة على بحر من بحور الشعر العربي الفصيح حول الموضوع التالي:
            - حدد اسم البحر الشعري والقافية والروي
            - اكتب أبياتاً عالية الجزالة والرقة
            - قدم التقطيع العروضي لبيت واحد من الأبيات (التفاعيل)
            
            الموضوع أو الطلب:
            $text
            """.trimIndent()
        }
    ),
    TRANSLATION(
        id = "translation",
        titleAr = "الترجمة اللغوية الثقافية",
        iconName = "Translate",
        subtitleAr = "ترجمة سياقية تنقل روح المعنى والأمثال بدون جمود حرفي",
        placeholderAr = "أدخل النص بالإنجليزية أو العربية لترجمته ترجمة أدبية راقية...",
        promptTemplate = { text ->
            """
            ترجم النص التالي ترجمة لغوية متقنة ورفيعة المستوى (ليست ترجمة آلية حرفية بل نقل دلالي وثقافي ثري):
            1. الترجمة الفصيحة المعتمدة.
            2. البدائل والتعابير الاصطلاحية المتطابقة ثقافياً.
            3. ملاحظات لغوية على التعبيرات الخاصة والمجاز.
            
            النص:
            $text
            """.trimIndent()
        }
    ),
    SUMMARIZATION(
        id = "summarization",
        titleAr = "التلخيص الدلالي واستخراج الرؤى",
        iconName = "Subject",
        subtitleAr = "تكثيف المعاني واستخلاص الأفكار الجوهرية والرسائل الكامنة",
        placeholderAr = "الصق المقال أو النص الطويل لتلخيصه بدقة...",
        promptTemplate = { text ->
            """
            لخص النص التالي تلخيصاً مركزاً بأسلوب عربي فصيح:
            - الفكرة المحورية الكبرى في سطر واحد
            - النقاط الرئيسة في نقاط موجزة ومتقنة
            - الخلاصات أو التوصيات
            - ثلاث كلمات مفتاحية تعبر عن روح النص
            
            النص:
            $text
            """.trimIndent()
        }
    )
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: String, // "user" or "model"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isStreaming: Boolean = false,
    val modelType: AiModelType = AiModelType.GEMINI_FLASH,
    val tokensEstimate: Int = 0,
    val latencyMs: Long? = null
)

data class ModelHyperparameters(
    val temperature: Float = 0.7f,
    val topP: Float = 0.95f,
    val topK: Int = 40,
    val thinkingLevel: String = "none", // "none", "low", "high"
    val persona: SystemPersona = SystemPersona.FASIH,
    val customSystemPrompt: String = "",
    val modelType: AiModelType = AiModelType.GEMINI_FLASH
)

data class SavedLinguisticItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val originalPrompt: String = "",
    val toolType: LinguisticToolType? = null,
    val timestamp: Long = System.currentTimeMillis()
)
