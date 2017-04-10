A tiny adventure game, implemented in various languages for practice.

Here's an example session:

    ## hallway

    This room feels very welcoming, with warm colors and a friendly atmosphere.
    There's a big huge door to the south, that beckons for you to open it.
    Exits are east.

    > east

    ## kitchen

    All neat and clean surfaces. Clearly this kitchen is tended by a professional.

    There is a key here.
    Exits are west.

    > take key

    You pick up the key.

    > west

    ## hallway

    > open door

    You unlock the door with the key.
    You open the door. There's now a new exit to the south.

    > look

    This room feels very welcoming, with warm colors and a friendly atmosphere.
    Cool air wafts in the open door to the south. It beckons for you walk through it.
    Exits are east, and south.

    > south

    You win the game. Thanks for playing!

## Languages to investigate

Might as well dump this information here, before it gets lost in the Slack backlog.

* Lisp (1958)
    * Because of its many "firsts", and the way it pushed the boundaries early.
    * Because the syntax is the AST, kinda. Homoiconicity.
    * Because of the power of functions and lambdas.
    * Because of self-hosting and the metacircular evaluator.
    * Because of Lisp macros.
    * Because of its early connections to AI.
* APL (1964)
    * Because of the "all the primitives built in" paradigm.
    * Because of the vector-based thinking.
* Logo (1967)
    * Because of the connection to children learning programming.
* Forth (1970)
    * Because of the stack-based paradigm.
    * Because of the refactoring ethos.
* Scheme (1970)
    * Because it drives home the point of lexical closures and lambdas.
    * Because it will never be friends with Common Lisp.
* C (1972)
    * Because it makes you think about memory management.
    * Because it's basically a universal assembly language for the von Neumann architecture.
* Prolog (1972)
    * Because of its logic programming paradigm.
* Smalltalk (1972)
    * Because of the connection to the birth of OOP.
    * Because of its "object purity".
    * Because nothing is fixed and everything is late-bound.
    * Because of the connection to children learning programming.
* SQL (1974)
    * Because it's the only fifth-generation language on the list (along with Prolog)
    * Because it has things to teach us about declarative syntax and "do what I mean"
    * Because relational algebra is interesting
    * Because the ways in which SQL isn't ideal (but still won big) are interesting
    * Because SQL's `NULL` and three-value logic are "interesting" and slightly controversial (but much used)
    * Because the transactional/ACID requirements on databases are interesting
* C++ (1983)
    * Because of the completely different take on OOP compared to Smalltalk.
    * Because it's still to this day in some sense relevant.
    * Because it combines OOP and manual memory management.
    * Because this is as good place a any to talk about vtables.
* Eiffel (1986)
    * Because of its interesting stance on DBC and invariants.
* Erlang (1986)
    * Because of its approach to distributed programming.
* Haskell (1990)
    * Because it is a good case of a lazy, functional language with type inference.
* Brainfuck (1993)
    * Because it shows it doesn't take a lot for a language to be Turing complete.
    * Because it shows that built-in higher-level abstractions matter.
* D (2001)
    * Because of the interesting perspectives it gives on C and C++
* Go (2009)
    * Because it highlights what a "modern C" could look like.
    * Because it favors compilation speed over a lot of other concerns.
    * Because it has interesting features for concurrency and parallelism.
    * Because it remains, in many regards, *boring* and stuck in the 1900s, and in many ways reflects its creators recurring preferences when creating languages.
    * Because it formats your code for you with an official formatter!
* Rust (2010)
    * Because it uses the type system to guarantee memory safety.
