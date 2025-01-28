import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.util.Arrays;

public class MainClassGUI extends Application {
    private static final String MODERN_STYLE = """
            .root {
                -fx-background-color: #f5f5f5;
            }
            .button {
                -fx-background-color: #2196F3;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 5;
            }
            .button:hover {
                -fx-background-color: #1976D2;
            }
            .text-field, .text-area, .combo-box {
                -fx-background-radius: 5;
                -fx-border-radius: 5;
                -fx-border-color: #E0E0E0;
                -fx-border-width: 1;
                -fx-padding: 8;
            }
            .label {
                -fx-font-size: 14px;
            }
            #title-label {
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #1565C0;
            }
            #result-panel {
                -fx-background-color: white;
                -fx-background-radius: 5;
                -fx-padding: 15;
                -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 10, 0, 0, 3);
            }
            """;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Algorithm Visualizer");

        // Create main container
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // Title
        Label titleLabel = new Label("Algorithm Visualizer");
        titleLabel.setId("title-label");

        // Create input section
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(15);
        inputGrid.setAlignment(Pos.CENTER);

        // Array size input with label
        Label arraySizeLabel = new Label("Array Size:");
        TextField arraySizeInput = new TextField();
        arraySizeInput.setPromptText("Enter size");
        arraySizeInput.setPrefWidth(150);

        // Array elements input with label
        Label elementsLabel = new Label("Array Elements:");
        TextArea arrayElementsInput = new TextArea();
        arrayElementsInput.setPromptText("Elements will appear here");
        arrayElementsInput.setPrefRowCount(2);
        arrayElementsInput.setWrapText(true);

        // Algorithm selection
        Label algorithmLabel = new Label("Algorithm:");
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
        algorithmChoice.setPromptText("Choose algorithm");
        algorithmChoice.setPrefWidth(200);

        // Target number input
        Label targetLabel = new Label("Search Target:");
        TextField targetNumberInput = new TextField();
        targetNumberInput.setPromptText("Enter number to search");
        targetNumberInput.setDisable(true);

        // Enable/disable target input based on algorithm selection
        algorithmChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null && (newValue.contains("Search"))) {
                targetNumberInput.setDisable(false);
                targetNumberInput.setStyle("-fx-opacity: 1");
            } else {
                targetNumberInput.setDisable(true);
                targetNumberInput.setStyle("-fx-opacity: 0.5");
            }
        });

        // Add components to grid
        inputGrid.add(arraySizeLabel, 0, 0);
        inputGrid.add(arraySizeInput, 1, 0);
        inputGrid.add(elementsLabel, 0, 1);
        inputGrid.add(arrayElementsInput, 1, 1);
        inputGrid.add(algorithmLabel, 0, 2);
        inputGrid.add(algorithmChoice, 1, 2);
        inputGrid.add(targetLabel, 0, 3);
        inputGrid.add(targetNumberInput, 1, 3);

        // Create button container
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);

        // Generate random array button
        Button generateRandomArrayButton = new Button("Generate Random Array");
        generateRandomArrayButton.setOnAction(e -> {
            try {
                int arraySize = Integer.parseInt(arraySizeInput.getText());
                int[] arr = new int[arraySize];
                for (int i = 0; i < arraySize; i++) {
                    arr[i] = (int) (Math.random() * 100);
                }
                arrayElementsInput.setText(Arrays.toString(arr)
                        .replaceAll("\\[|\\]", "")
                        .replaceAll(",", " "));
            } catch (NumberFormatException ex) {
                showError("Please enter a valid array size");
            }
        });

        // Execute button
        Button executeButton = new Button("Execute Algorithm");
        executeButton.setDefaultButton(true);

        buttonContainer.getChildren().addAll(generateRandomArrayButton, executeButton);

        // Result panel
        VBox resultPanel = new VBox(10);
        resultPanel.setId("result-panel");
        resultPanel.setMaxWidth(600);

        Label resultTitle = new Label("Result");
        resultTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        Label resultLabel = new Label("Execute an algorithm to see results");
        resultLabel.setWrapText(true);

        resultPanel.getChildren().addAll(resultTitle, resultLabel);

        // Add execute button functionality
        executeButton.setOnAction(e -> {
            try {
                int arraySize = Integer.parseInt(arraySizeInput.getText());
                int[] arr = Arrays.stream(arrayElementsInput.getText().split("\\s+"))
                        .map(String::trim)
                        .mapToInt(Integer::parseInt)
                        .toArray();

                if (arr.length != arraySize) {
                    showError("Number of elements doesn't match the specified array size");
                    return;
                }

                String algorithm = algorithmChoice.getValue();
                if (algorithm == null || algorithm.isEmpty()) {
                    showError("Please select an algorithm");
                    return;
                }

                String result = executeAlgorithm(arr, algorithm, targetNumberInput);

                // Animate result update
                FadeTransition fade = new FadeTransition(Duration.millis(300), resultLabel);
                fade.setFromValue(0);
                fade.setToValue(1);
                resultLabel.setText(result);
                fade.play();

            } catch (NumberFormatException ex) {
                showError("Please enter valid numbers");
            } catch (Exception ex) {
                showError("An error occurred: " + ex.getMessage());
            }
        });

        // Add all components to main container
        mainContainer.getChildren().addAll(
                titleLabel,
                inputGrid,
                buttonContainer,
                resultPanel
        );

        // Create scene with styling
        Scene scene = new Scene(mainContainer, 800, 600);
        scene.getStylesheets().add("data:text/css," + MODERN_STYLE.replaceAll("\n", ""));

        // Configure and show stage
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(850);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    private String getTimeComplexity(String algorithm) {
        return switch (algorithm) {
            case "Selection Sort" -> "O(n²) - Quadratic time";
            case "Insertion Sort" -> "O(n²) - Quadratic time, O(n) for nearly sorted arrays";
            case "Quick Sort" -> "O(n log n) average case, O(n²) worst case";
            case "Merge Sort" -> "O(n log n) - Consistent performance";
            case "Radix Sort" -> "O(d * n) where d is the number of digits";
            case "Linear Search" -> "O(n) - Linear time";
            case "Binary Search" -> "O(log n) - Logarithmic time";
            default -> "Unknown";
        };
    }

    private String executeAlgorithm(int[] arr, String algorithm, TextField targetNumberInput) {
        int[] originalArray = arr.clone();
        String result;

        // Get theoretical time complexity
        String timeComplexity = getTimeComplexity(algorithm);

        // Measure execution time
        long startTime = System.nanoTime();

        switch (algorithm) {
            case "Selection Sort":
                SelectionSort.selectionSort(arr);
                result = formatSortResult(originalArray, arr, "Selection Sort");
                break;
            case "Insertion Sort":
                InsertionSort.insertionSort(arr);
                result = formatSortResult(originalArray, arr, "Insertion Sort");
                break;
            case "Quick Sort":
                QuickSort.quickSort(arr, 0, arr.length - 1);
                result = formatSortResult(originalArray, arr, "Quick Sort");
                break;
            case "Merge Sort":
                MergeSort.mergeSort(arr, 0, arr.length - 1);
                result = formatSortResult(originalArray, arr, "Merge Sort");
                break;
            case "Radix Sort":
                RadixSort.radixSort(arr);
                result = formatSortResult(originalArray, arr, "Radix Sort");
                break;
            case "Linear Search":
            case "Binary Search":
                int target = Integer.parseInt(targetNumberInput.getText());
                int searchResult = algorithm.equals("Linear Search")
                        ? LinearSearch.linearSearch(arr, target)
                        : BinarySearch.binarySearch(arr, target);
                result = formatSearchResult(arr, target, searchResult, algorithm);
                break;
            default:
                result = "Invalid algorithm selection";
        }

        // Calculate execution time
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds

        // Add complexity and timing information to result
        return result + String.format("""
                
                Time Complexity: %s
                Execution Time: %.2f ms""", timeComplexity, executionTime);
    }

    private String formatSortResult(int[] original, int[] sorted, String algorithm) {
        return String.format("""
                Algorithm: %s
                Original array: %s
                Sorted array: %s""",
                algorithm,
                Arrays.toString(original),
                Arrays.toString(sorted));
    }

    private String formatSearchResult(int[] arr, int target, int result, String algorithm) {
        return String.format("""
                Algorithm: %s
                Array: %s
                Target: %d
                Result: %s""",
                algorithm,
                Arrays.toString(arr),
                target,
                result != -1 ? "Found at index " + result : "Element not found");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
