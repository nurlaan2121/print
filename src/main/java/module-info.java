module print.print {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    opens print.print.dto to com.fasterxml.jackson.databind;
    opens print.print to javafx.fxml;

    exports print.print.dto;
    exports print.print;
}