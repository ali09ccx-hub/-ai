# بيان AI | Bayan AI 🖋️✨
### نموذج واستوديو الذكاء الاصطناعي اللغوي العربي المتطور
**Advanced Arabic Large Language Model Studio & Assistant for Android**

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Gemini AI](https://img.shields.io/badge/AI%20Engine-Gemini%203.5%20%26%203.1-FFB900?style=for-the-badge&logo=google&logoColor=black)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

---

## 📖 نبذة عن التطبيق | Overview

**بيان AI** هو تطبيق أندرويد متكامل صُمم ليكون نموذج واستوديو ذكاء اصطناعي لغوي فائق التخصص في علوم لغة الضاد الشريفة وبلاغتها وأدبها. يجمع التطبيق بين أحدث خوارزميات الذكاء الاصطناعي التوليدي (**Gemini 3.5 Flash** و **Gemini 3.1 Pro Preview**) وأرقى تصاميم **Material Design 3** بدعم كامل وشامل للاتجاه من اليمين لليسار (**RTL**) والخط العربي الفاخر (**Cairo**).

---

## ✨ المميزات الرئيسة | Key Features

### 1. 💬 الحوار اللغوي الذكي المباشر (Conversational Engine)
- استجابة سريعة مع **بث حي فوري (Live Streaming)** للردود حرفاً بحرف.
- حفظ سياق المحادثة وتعدد الأدوار (Multi-turn Context).
- إحصاءات لحظية لزمن الاستجابة (Latency) وعدد الرموز المقدر (Token count).
- شرائح أوامر سريعة مقترحة (Quick Prompt Chips) تتيح استكشاف قدرات النموذج بلمسة واحدة.
- نسخ النصوص، وتنسيق الماركداون الغني مع تمييز العناوين والشعر والاقتباسات.

### 2. 🔬 المختبر اللغوي المتخصص (Specialized Linguistic Lab)
- **التشكيل وضبط الأواخر**: تشكيل الحركات الإعرابية التامة للنصوص مع مراعاة الموقع الإعرابي السليم.
- **الإعراب والتحليل النحوي**: إعراب الجمل، الآيات القرآنية، والأبيات الشعرية إعراباً تفصيلياً وافياً.
- **التدقيق والتهذيب البلاغي**: رفع ركاكة التراكيب، واقتراح بدائل أبلغ وأرقى، مع بيان مواطن البلاغة والمجاز.
- **نظم الشعر وأوزان الخليل**: نظم أبيات وقصائد فصيحة على بحور الشعر العربي (البسيط، الطويل، الكامل، الوافر...) مع التقطيع العروضي.
- **الترجمة الدلالية والثقافية**: نقل المعاني المجازية والأمثال الثقافية بروح عربية راقية بعيداً عن الترجمة الحرفية الجافة.
- **التلخيص الدلالي**: تكثيف المقالات والنصوص الطويلة واستخلاص الفكرة المحورية والرؤى الجوهرية.

### 3. ⚙️ مختبر المعايير وهندسة الأوامر (Model Playground)
- التبديل السلس بين المحركات:
  - **بيان فلاش 3.5 (`gemini-3.5-flash`)**: للسرعة الفائقة والمهام اللحظية.
  - **بيان برو 3.1 (`gemini-3.1-pro-preview`)**: للاستدلال المنطقي المعقد والنقد الأدبي العميق.
- التحكم بدقة في معاملات التوليد:
  - **درجة الحرارة (Temperature)**: من الدقة الصارمة (0.0) إلى الإبداع الشعري الجامح (1.5).
  - **نطاق الاحتمالية (Top-P)** و **(Top-K)**.
  - **مستوى التفكير والتأمل (Thinking Level)**: مباشر، معتدل، أو استدلال عميق.
  - **المقام والتوجيه النظامي (System Persona)**: العلاّمة، الأكاديمي، الشاعر، المحرر الرسمي، المعلم، أو مقام مخصص بالكامل.

### 4. 📚 المحفوظات والذخائر (Saved Masterpieces)
- حفظ أي قصيدة أو إعراب أو دراسة بنقرة زر.
- محرك بحث داخلي سريع للوصول إلى النصوص المحفوظة وإعادة نسخها أو تصديرها.

---

## 🛠️ البنية التقنية | Tech Stack & Architecture

- **Language:** Kotlin 100%
- **UI Framework:** Jetpack Compose (Declarative UI)
- **Design System:** Material Design 3 (M3) + Custom Luxury Dark Palette (`#0B192C`, `#E5A93C`, `#38BDF8`)
- **Typography:** Google Font **Cairo** bundled locally
- **Architecture:** Clean MVVM (Model-View-ViewModel) with Kotlin Coroutines & StateFlow
- **Networking:** Retrofit 2 + OkHttp 4 (with 60s timeout & SSE Stream reader)
- **Serialization:** Kotlinx Serialization
- **Iconography:** Custom Adaptive Icon with 3D render & Android 13+ theming support

---

## 🚀 تشغيل وتطوير المشروع | Getting Started

### المتطلبات الأساسية (Prerequisites)
- **Android Studio** (Koala / Ladybug أو أحدث)
- **JDK 17**
- جهاز بنظام **Android 7.0 (API 24)** أو أحدث

### خطوات التثبيت والتشغيل:

1. **استنساخ المستودع (Clone repository):**
   ```bash
   git clone https://github.com/YOUR_USERNAME/bayan-ai.git
   cd bayan-ai
   ```

2. **ضبط مفتاح Gemini API:**
   - انسخ ملف `.env.example` إلى `.env`:
     ```bash
     cp .env.example .env
     ```
   - أضف مفتاح API الخاص بك من Google AI Studio:
     ```properties
     GEMINI_API_KEY=AIzaSy...YourKeyHere
     ```
   *(إذا كنت تعمل داخل Google AI Studio، يتم حقن المفتاح تلقائياً عبر لوحة Secrets).*

3. **بناء المشروع (Build APK):**
   ```bash
   # بناء نسخة التجربة (Debug APK)
   gradle assembleDebug

   # تشغيل الاختبارات
   gradle testDebugUnitTest
   ```

4. **تصدير ونشر المشروع إلى GitHub:**
   ```bash
   git init
   git add .
   git commit -m "Initial commit: Bayan AI Arabic Large Language Model Studio"
   git branch -M main
   git remote add origin https://github.com/YOUR_USERNAME/bayan-ai.git
   git push -u origin main
   ```

---

## 📄 الترخيص | License

هذا المشروع مرخص بموجب رخصة **MIT** - راجع ملف [LICENSE](LICENSE) لمزيد من التفاصيل.
