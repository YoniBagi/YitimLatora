package com.yitimlatora.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yitimlatora.R

@Composable
fun AboutScreen(navController: NavController) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // About Rabbi Yaron Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Rabbi Yaron's photo
                        Image(
                            painter = painterResource(id = R.drawable.yaron),
                            contentDescription = "הרב ירון",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            "אודות הרב",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            "הרב ירון אליה משמש כרב בית הכנסת רינת ירושלים שירת עמ\"י ביד בנימין וכראש הכולל שם.\n" +
                                    "\n" +
                                    "במסגרת שיעוריו בהלכה לומדים זה לצד זה תלמידי חכמים שמוסרים שיעורים בעצמם, עם גמלאי צה\"ל ובעלי מקצועות חופשיים מגוונים שמרגישים תועלת גדולה בלימוד עם הרב.\n" +
                                    "\n" +
                                    "הרב מוסמך לרבנות עיר והוציא מספר ספרים לתועלת לומדי ההלכה בעיון ולנערכים למבחני הרבנות. כמו כן הרב כותב חוברות הלכה לישוב לפני כל מועד המפרטים את ההלכה למעשה למנהגים השונים.\n" +
                                    "\n" +
                                    "לפני שהרב התמקד בלימוד ומסירת התורה הוא למד הנדסה ומנהל עסקים ושימש בתפקידים טכנולוגיים ועסקיים בצה\"ל ובהייטק. ומתוך זה קנה יכולת לחבר את עולם התורה גם לאנשים שנמצאים בעולם המעשה.\n" +
                                    "\n" +
                                    "למרות השינוי שהרב עשה בחייו הוא מאמין שהדרך היותר נכונה לכל אדם היא לשלב חיי עבודה מלאים על פי כישוריו, עם קביעת עיתים משמעותית בתורה בהתאם לאותם כישורים שהקב\"ה חנן אותו בהם.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            
            // About the Website Section
            item {
                Text(
                    "אודות האתר",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
            
            // Vision Section
            item {
                AboutSectionCard(
                    title = "הנהגת האדם בלימוד התורה",
                    content = "ידועה מחלוקת רבי ישמעאל ורבי שמעון בדרך ההנהגה הנכונה לאדם לשילוב לימוד התורה בחייו. ומסקנת הגמרא ' הרבה עשו כרבי ישמעאל - ועלתה בידן, כרבי שמעון בן יוחי - ולא עלתה בידן'.\n" +
                            "\n" +
                            "נמצא שהוראת הגמרא לרבים היא כדברי ר' ישמעאל 'הנהג בהן מנהג דרך ארץ'. כלומר לשלב זמנים משמעותיים של לימוד תורה בזמנים משמעותיים של פרנסת בני ביתו.\n" +
                            "\n" +
                            "וכדברי השו\"ע בסימן קנ\"ה 'אחר שיצא מבית הכנסת, ילך לבית המדרש; ויקבע עת ללמוד, וצריך שאותו עת יהיה קבוע שלא יעבירנו אף אם הוא סבור להרויח הרבה' ובהמשך בסימן קנ\"ו כתב השו\"ע  'אח\"כ ילך לעסקיו, דכל תורה שאין עמה מלאכה סופה בטלה וגוררת עון, כי העוני יעבירנו ע\"ד קונו. ומ\"מ לא יעשה מלאכתו עיקר, אלא עראי, ותורתו קבע, וזה וזה יתקיים בידו'."                )
            }
            
            // Content Section
            item {
                AboutSectionCard(
                    title = "מטרת האתר והשיעורים בו",
                    content = "אתר זה בא לתרום את תרומתו לאנשים שעמלים לקיים את חובתם ההלכתית לפרנס את נשיהם וילדיהם ומחפשים מסגרת שתסייע להם לקיים את חובתם כלפי שמים בלימוד תורה משמעותי במהלך היום.\n" +
                            "\n" +
                            "השיעורים באתר ניתנים 'בעיון קל' כלומר מכילים את יסודות ההלכה כמובא בגמרא ובראשונים ועד פוסקי זמנינו על מנת שתהיה ברורה ההלכה למעשה ביחד עם שורשיה במקורות, מה שמאפשר לאנשים שזכו ללימוד תורה משמעותי קודם שיצאו לפרנסתם לטעום מטעמה שוב. ועדיין נמסרים ללא פלפול ובאופן שגם אנשים שלא מורגלים לספסלי בית המדרש יוכלו להנות ממנו.\n" +
                            "\n" +
                            "וכבר כתב הט\"ז ביו\"ד סימן רמ\"ו שעיקר לימוד התורה בקביעת עיתים צריך להיות בדברי הפוסקים להלכה, וכן הסכימו אחריו כל פוסקי זמנינו. "                )
            }
            
            // Mission Section
            item {
                AboutSectionCard(
                    title = "דרך הלימוד באתר",
                    content = "הגם שניתן ללמוד בשיעורים תוך כדי נסיעה 'בלכתך בדרך' מומלץ להקדיש זמן ביום בו ניתן לשבת בנחת מול המחשב ולהתמסר ללימוד התורה תוך הקשבה וקריאת דף המקורות במקביל.\n" +
                            "\n" +
                            "השיעורים מכילים דף מקורות מושקע המכיל את דברי הפוסקים מהגמרא עד להלכה למעשה שנקראים ומוסברים כולם על ידי הרב. יש תועלת גדולה לאדם בראיית דברי רבותינו הפוסקים בלשונם, ובמיקוד מלא בשיעור בבחינת 'תורתו קבע' כלומר שיהיה זמן ביום שהוא קבוע כולו בתורתו.\n" +
                            "\n" +
                            "כמו כן האתר מכיל ספרים ללימוד ועיון בהלכה, וכן תקצירי הלכה המאפשרים לכל אדם ללמוד על פי כוחותיו וכישוריו.\n" +
                            "\n" +
                            "מקוים שהאתר יהיה לתועלת ללומדים ושמחים לקבל כל הערה ובקשה דרך 'צור קשר' או במייל info@itim-latora.org"                )
            }
        }
    }
}

@Composable
fun AboutSectionCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    MaterialTheme {
        AboutScreen(navController = rememberNavController())
    }
}

