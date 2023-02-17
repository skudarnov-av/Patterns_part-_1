package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.deliveryCard.DataGenerator;

import java.time.Duration;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;



public class DeliveryTest {

    private final DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
    private final int daysToAdd = 5;
    private final int daysToAddSeven = 7;
    private final String firstMeetingDate = DataGenerator.generateDate(daysToAdd);

    private final String firstMeetingDateSeven = DataGenerator.generateDate(daysToAddSeven);


    @BeforeEach
    void setUpBeforeEach() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
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

        //Вторая часть.
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDateSeven);

        $(byText("Запланировать")).click();

        $("[data-test-id=\"replan-notification\"]").shouldHave(Condition.text("Необходимо подтверждение У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(byText("Перепланировать")).click();

        //$("[data-test-id=\"success-notification\"]").shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + firstMeetingDateSeven), Duration.ofSeconds(15));
    }
}
