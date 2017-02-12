import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

class GameState {
    String playerLocation;
    Set<String> seen = new HashSet<>();
    boolean holdingKey = false;
}

class Command implements Predicate<GameState>, Consumer<GameState> {
    String primaryInput;
    Predicate<GameState> condition;
    Consumer<GameState> action;
    List<String> inputSynonyms;

    Command(String primaryInput, Predicate<GameState> condition, Consumer<GameState> action, String... inputSynonyms) {
        this.primaryInput = primaryInput;
        this.condition = condition;
        this.action = action;
        this.inputSynonyms = asList(inputSynonyms);
    }

    private static <T> boolean intersect(List<T> list1, List<T> list2) {
        return new ArrayList<>(list1).removeAll(list2);
    }

    boolean recognizes(String... alternatives) {
        return asList(alternatives).contains(primaryInput)
            || intersect(asList(alternatives), inputSynonyms);
    }

    public boolean test(GameState state) {
        return condition.test(state);
    }

    public void accept(GameState state) {
        action.accept(state);
    }
}

class TinyAdventure {
    GameState state = new GameState();

    Map<String, String> description = new HashMap<>(); {
        description.put("hallway", "This room feels very welcoming, with warm colors and a friendly atmosphere.\n" +
                                    "There's a big huge door to the south, that beckons for you to open it.");
        description.put("kitchen", "All neat and clean surfaces. Clearly this kitchen is tended by a professional.");
    }

    Predicate<GameState> inHallway = (state) -> state.playerLocation.equals("hallway");
    Predicate<GameState> inKitchen = (state) -> state.playerLocation.equals("kitchen");
    Predicate<GameState> holdingKey = (state) -> state.holdingKey;

    String[] pickUpKeySynonyms = new String[] {
        "take key",
        "grab key",
        "get key",
    };

    String[] openDoorSynonyms = new String[] {
        "open door",
        "unlock door",
        "use key",
        "use key in door",
        "use key to unlock door",
        "use key to open door",
    };

    List<Command> commands = new ArrayList<>(asList(new Command[] {
        new Command("look", inHallway.or(inKitchen), (state) -> look(), "l"),

        new Command("east", inHallway, ($) -> enter("kitchen"), "e"),
        new Command("west", inKitchen, ($) -> enter("hallway"), "w"),

        new Command("pick up key", inKitchen.and(holdingKey.negate()), (state) -> {
            System.out.println("You pick up the key.");
            state.holdingKey = true;
        }, pickUpKeySynonyms),

        new Command("pick up key", holdingKey, ($) -> {
            System.out.println("You already have the key!");
        }, pickUpKeySynonyms),

        new Command("use key on door", inHallway.and(holdingKey), ($0) -> {
            System.out.println("You unlock the door with the key.");
            System.out.println("You open the door. There's now a new exit to the south.");

            description.put("hallway", "This room feels very welcoming, with warm colors and a friendly atmosphere.\n" +
                                    "Cool air wafts in the open door to the south. It beckons for you walk through it.");

            addCommand(new Command("south", inHallway, ($1) -> {
                System.out.println("You win the game. Thanks for playing!");
                System.exit(0);
            }, "s"));
        }, openDoorSynonyms),

        new Command("use key on door", inHallway, ($) -> {
            System.out.println("You do not have the key!");
        }, openDoorSynonyms),
    }));

    Command QUIT_COMMAND = new Command("quit", null, null, "exit", "q");

    private void addCommand(Command command) {
        commands.add(command);
    }

    void enter(String room) {
        state.playerLocation = room;
        System.out.println("## " + room);

        if (!state.seen.contains(room)) {
            System.out.println();
            look();
            state.seen.add(room);
        }
    }

    void look() {
        System.out.println(description.get(state.playerLocation));

        if (state.playerLocation.equals("kitchen") && !state.holdingKey) {
            System.out.println();
            System.out.println("There is a key here.");
        }

        String exits = commands.stream()
            .filter((command) -> command.recognizes("east", "west", "south") && command.test(state))
            .map((command) -> command.primaryInput)
            .collect(Collectors.joining(", and "));
        System.out.println("Exits are " + exits + ".");
    }

    String prompt() {
        String input = System.console().readLine("> ");
        System.out.println();
        return input;
    }

    public static void main(String[] args) {
        new TinyAdventure().run();
    }

    void run() {
        enter("hallway");
        System.out.println();

        while (true) {
            String input = prompt();
            if (input == null || QUIT_COMMAND.recognizes(input)) {
                break;
            }

            commands.stream()
                .filter((command) -> command.recognizes(input) && command.test(state))
                .map((command) -> command.action)
                .findFirst().orElse(($) -> {
                    boolean commandValidButNotHere = commands.stream()
                        .anyMatch((command) -> command.recognizes(input));
                    System.out.println(commandValidButNotHere
                        ? "You can't do that here!"
                        : "Unknown command; sorry.");
                })
                .accept(state);

            System.out.println();
        }

        System.out.println("Thanks for playing. Bye!");
    }
}
