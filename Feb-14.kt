/*
    It is completely normal that these concepts feel a bit weird coming from
    C++.

    In C++, if you define a class inside another class (e.g.,
    `class Outer { class Inner{}; };`, it is purely a scoping mechanism
    (`Outer::Inner`). The `Inner` class has NO IDEA about the actual instance of
    `Outer` unless you manually pass a pointer to it
* */