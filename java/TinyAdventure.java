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
    String room;
    Set<String> seen = new HashSet<>();
    boolean holdingKey = false;
}

class Command implements Predicate<GameState>, Consumer<GameState> {
    String input;
    Predicate<GameState> condition;
    Consumer<GameState> action;

    Command(String input, Predicate<GameState> condition, Consumer<GameState> action) {
        this.input = input;
        this.condition = condition;
        this.action = action;
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

    Predicate<GameState> everywhere = (state) -> true;
    Predicate<GameState> inHallway = (state) -> state.room.equals("hallway");
    Predicate<GameState> inKitchen = (state) -> state.room.equals("kitchen");
    Predicate<GameState> holdingKey = (state) -> state.holdingKey;

    Consumer<GameState> pickUpKey = (state) -> {
        System.out.println("You pick up the key.");
        state.holdingKey = true;
    };
    Consumer<GameState> winTheGame = (state) -> {
        System.out.println("You win the game. Thanks for playing!");
        System.exit(0);
    };
    Consumer<GameState> unlockDoor = (state) -> {
        System.out.println("You unlock the door with the key.");
        System.out.println("You open the door. There's now a new exit to the south.");
        description.put("hallway", "This room feels very welcoming, with warm colors and a friendly atmosphere.\n" +
                                    "There's a big huge open door to the south, that beckons for you walk through it.");
        getCommands().add(new Command("south", inHallway, winTheGame));
    };
    Consumer<GameState> youDoNotHaveTheKey = (state) -> {
        System.out.println("You do not have the key!");
    };
    Consumer<GameState> thereIsNoDoorHere = (state) -> {
        System.out.println("There is no door here! Well, none that matters, anyway.");
    };

    List<Command> commands = new ArrayList<>(asList(new Command[] {
        new Command("look", everywhere, (state) -> look()),
        new Command("east", inHallway, (state) -> enter("kitchen")),
        new Command("west", inKitchen, (state) -> enter("hallway")),
        new Command("pick up key", inKitchen, pickUpKey),
        new Command("take key", inKitchen, pickUpKey),
        new Command("grab key", inKitchen, pickUpKey),
        new Command("get key", inKitchen, pickUpKey),
        new Command("open door", inHallway.and(holdingKey), unlockDoor),
        new Command("unlock door", inHallway.and(holdingKey), unlockDoor),
        new Command("use key", inHallway.and(holdingKey), unlockDoor),
        new Command("use key on door", inHallway.and(holdingKey), unlockDoor),
        new Command("open door", inHallway, youDoNotHaveTheKey),
        new Command("unlock door", inHallway, youDoNotHaveTheKey),
        new Command("use key", inHallway, youDoNotHaveTheKey),
        new Command("use key on door", inHallway, youDoNotHaveTheKey),
        new Command("open door", inKitchen, thereIsNoDoorHere),
        new Command("unlock door", inKitchen, thereIsNoDoorHere),
        new Command("use key", inKitchen, thereIsNoDoorHere),
        new Command("use key on door", inKitchen, thereIsNoDoorHere),
    }));

    List<Command> getCommands() {
        return commands;
    }

    static Command UNKNOWN_COMMAND = new Command(null, null, (state) -> {
        System.out.println("Unknown command; sorry.");
    });

    void enter(String room) {
        state.room = room;
        System.out.println("## " + state.room);

        if (!state.seen.contains(state.room)) {
            System.out.println();
            look();
            state.seen.add(state.room);
        }
    }

    void look() {
        System.out.println(description.get(state.room));

        if (state.room.equals("kitchen") && !state.holdingKey) {
            System.out.println();
            System.out.println("There is a key here.");
        }

        System.out.println("Exits are " + exits() + ".");
    }

    String exits() {
        return commands.stream()
            .filter((command) -> command.input.equals("east")
                        || command.input.equals("west")
                        || command.input.equals("south"))
            .filter((command) -> command.test(state))
            .map((command) -> command.input)
            .collect(Collectors.joining(", "));
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
            if (input == null || input.equals("quit")) {
                break;
            }

            commands.stream()
                .filter((command) -> command.input.equals(input))
                .filter((command) -> command.test(state))
                .findFirst().orElse(UNKNOWN_COMMAND)
                .accept(state);

            System.out.println();
        }

        System.out.println("Thanks for playing. Bye!");
    }
}
