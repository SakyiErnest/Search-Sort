
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;

public class MainClassGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sorting and Searching Algorithms");

        // Create input field for array size
        TextField arraySizeInput = new TextField();
        arraySizeInput.setPromptText("Enter the number of elements");

        // Create input field for array elements
        TextArea arrayElementsInput = new TextArea();
        arrayElementsInput.setPromptText("Enter the elements");

        // Create dropdown for algorithm selection
        ComboBox<String> algorithmChoice = new ComboBox<>();
        algorithmChoice.getItems().addAll(
                "Selection Sort",
                "Insertion Sort",
                "Quick Sort",
                "Merge Sort",
                "Radix Sort",
                "Linear Search",
                "Binary Search"
        );
        algorithmChoice.setPromptText("Choose the algorithm");

        // Create input field for target number (initially disabled)
        TextField targetNumberInput = new TextField();
        targetNumberInput.setPromptText("Enter the number to search");
        targetNumberInput.setDisable(true);

        // Enable target number input if Linear Search or Binary Search is selected
        algorithmChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue.equals("Linear Search") || newValue.equals("Binary Search")) {
                targetNumberInput.setDisable(false);
            } else {
                targetNumberInput.setDisable(true);
            }
        });

        // Create a label to display the results
        Label resultLabel = new Label();

        // Create a button to execute the selected algorithm
        Button executeButton = new Button("Execute");
        executeButton.setOnAction(e -> {
            try {
                int arraySize = Integer.parseInt(arraySizeInput.getText());

                int[] arr = Arrays.stream(arrayElementsInput.getText().split(","))
                        .map(String::trim)
                        .mapToInt(Integer::parseInt)
                        .toArray();

                // Check if array size matches user's input
                if (arr.length != arraySize) {
                    resultLabel.setText("The number of elements does not match the specified array size.");
                    return;
                }

                String algorithm = algorithmChoice.getValue(); // Get chosen algorithm

                if (algorithm == null || algorithm.isEmpty()) {
                    resultLabel.setText("Please select an algorithm.");
                    return;
                }

                int target = targetNumberInput.isDisabled() ? -1 : Integer.parseInt(targetNumberInput.getText());
                String result = "";
                switch (algorithm) {
                    case "Selection Sort":
                        SelectionSort.selectionSort(arr);
                        result = "Sorted array using Selection Sort: " + Arrays.toString(arr);
                        break;
                    case "Insertion Sort":
                        InsertionSort.insertionSort(arr);
                        result = "Sorted array using Insertion Sort: " + Arrays.toString(arr);
                        break;
                    case "Quick Sort":
                        QuickSort.quickSort(arr, 0, arr.length - 1);
                        result = "Sorted array using Quick Sort: " + Arrays.toString(arr);
                        break;
                    case "Merge Sort":
                        MergeSort.mergeSort(arr, 0, arr.length - 1);
                        result = "Sorted array using Merge Sort: " + Arrays.toString(arr);
                        break;
                    case "Radix Sort":
                        RadixSort.radixSort(arr);
                        result = "Sorted array using Radix Sort: " + Arrays.toString(arr);
                        break;
                    case "Linear Search":
                        int linearSearchResult = LinearSearch.linearSearch(arr, target);
                        if (linearSearchResult != -1) {
                            result = "Element found at index " + linearSearchResult;
                        } else {
                            result = "Element not found";
                        }
                        break;
                    case "Binary Search":
                        int binarySearchResult = BinarySearch.binarySearch(arr, target);
                        if (binarySearchResult != -1) {
                            result = "Element found at index " + binarySearchResult;
                        } else {
                            result = "Element not found";
                        }
                        break;
                }
                resultLabel.setText(result);

            } catch (NumberFormatException exception) {
                resultLabel.setText("Please enter valid numbers, separated by commas, and a valid array size.");
            } catch (Exception exception) {
                resultLabel.setText("An error occurred.");
            }
        });

        // Create a button to generate random array
        Button generateRandomArrayButton = new Button("Generate Random Array");
        generateRandomArrayButton.setOnAction(e -> {
            int arraySize = Integer.parseInt(arraySizeInput.getText());
            int[] arr = new int[arraySize];
            for (int i = 0; i < arraySize; i++) {
                arr[i] = (int) (Math.random() * 100);
            }
            arrayElementsInput.setText(Arrays.toString(arr).replaceAll("\\[|\\]|,", ""));
        });
