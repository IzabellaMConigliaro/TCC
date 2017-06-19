package Main;

import Model.DBTable;
import Model.SelectionMethod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import static Main.ArffConv.*;

public class Controller implements javafx.fxml.Initializable {
    Thread thread;

    @FXML
    ToggleGroup datasetGroup;
    @FXML
    ToggleGroup methodGroup;
    @FXML
    CheckBox limit_rows;
    @FXML
    CheckBox stop_words;
    @FXML
    TextField rows_limited;
    @FXML
    RadioButton db_comments;
    @FXML
    RadioButton db_felipe_melo;
    @FXML
    RadioButton db_pang_lee;
    @FXML
    RadioButton method_single_occurrence;
    @FXML
    RadioButton method_dictionary_value;
    @FXML
    RadioButton method_none;
    @FXML
    ProgressBar progress_bar;
    @FXML
    TextArea text_area;

    public void startConversion(ActionEvent actionEvent) {
        initRadioButtons();

        if (datasetGroup.getSelectedToggle() != null && methodGroup.getSelectedToggle() != null) {
            setDataset(DBTable.Dataset.getValue(Integer.parseInt(datasetGroup.getSelectedToggle().getUserData().toString())));
            setSelectionMethod(SelectionMethod.getValue(Integer.parseInt(methodGroup.getSelectedToggle().getUserData().toString())));
            setRemoveStopWords(stop_words.isSelected());
            if (limit_rows.isSelected()) {
                setLimitQuerySelect(Integer.parseInt(rows_limited.getText()));
            }

            Runnable runnable = new ValidateThread();
            thread = new Thread(runnable);
            thread.start();
        }
    }

    private void initRadioButtons() {
        db_comments.setUserData("1");
        db_felipe_melo.setUserData("2");
        db_pang_lee.setUserData("3");

        method_single_occurrence.setUserData("1");
        method_dictionary_value.setUserData("2");
        method_none.setUserData("0");
    }

    public void setTextArea(String progress) {
        text_area.setText(text_area.getText() + "\n" + progress);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArffConv.setController(this);
    }
}
