package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.deliveryCard.DeliveryCard;

import java.time.Duration;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;



public class DeliveryTest {

    private final DeliveryCard.UserInfo validUser = DeliveryCard.Registration.generateUser("ru");
    private final int daysToAdd = 5;
    private final int daysToAddSeven = 7;
    private final String firstMeetingDate = DeliveryCard.generateDate(daysToAdd);

    private final String firstMeetingDateSeven = DeliveryCard.generateDate(daysToAddSeven);

    @BeforeAll
    static void setUpBeforeAll() {
    }
    @BeforeEach
    void setUpBeforeEach() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }
    @AfterAll
    static void setUpAfterAll() {
    }
    @Test
    @DisplayName("Positive test")
    public void testPositive() {
        $("[data-test-id=\"city\"] input").setValue(validUser.getCity());
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").setValue(validUser.getName());
        $("[data-test-id=\"phone\"] input").setValue(validUser.getPhone());

        $("[data-test-id=\"agreement\"]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=\"success-notification\"]")
                .shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
    }
    @Test
    @DisplayName("Negative testPhone")
    public void testNegativePhone() {
        $("[data-test-id=\"city\"] input").setValue(validUser.getCity());
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").setValue(validUser.getName());

        $("[data-test-id=\"agreement\"]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=\"phone\"] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
    @Test
    @DisplayName("Negative testCity")
    public void testNegativeCity() {
        $("[data-test-id=\"city\"] input").setValue("Бангкок");
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").setValue(validUser.getName());

        $("[data-test-id=\"agreement\"]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=\"city\"] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void testPositiveReschedule() {
        $("[data-test-id=\"city\"] input").setValue(validUser.getCity());
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").setValue(validUser.getName());
        $("[data-test-id=\"phone\"] input").setValue(validUser.getPhone());

        $("[data-test-id=\"agreement\"]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=\"success-notification\"]")
                .shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
        //Вторая часть.
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDateSeven);

        $(byText("Запланировать")).click();

        $("[data-test-id=\"replan-notification\"]").shouldHave(Condition.text("Необходимо подтверждение У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(byText("Перепланировать")).click();

        $("[data-test-id=\"success-notification\"]").shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + firstMeetingDateSeven), Duration.ofSeconds(15));
    }
}
