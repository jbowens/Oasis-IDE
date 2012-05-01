(*Procedure: guess*)

(*Type signature*)
(*guess: number, number, number -> number*)

(*Contract:*)
(*guess takes as input a number, a lower bound, and an uper bound, and tries to guess the number by starting at the lower bound and counting up to the upper bound.  It returns the number of guesses it makes.*)

let guess num l u = 
  let rec guessh l u num counter = 
    match counter with
      |x when (x = u) -> if (u = num) then ((u - l) + 1) else failwith "The number is not contained within these bounds."
      |x -> if (counter = num) then ((counter - l) + 1) else (guessh l u num (counter + 1))
      in (guessh l u num l);;

(*test cases*)
(guess 3 1 10) = 3;;
(guess 10 1 11) = 10;;
(guess 9 2 9) = 8;;

(*Procedure: guess2*)

(*Type signature:*)
(*guess2: number, number, number -> number*)

(*Contract*)
(*guess2 takes as input a number, a lower bound, and an upper bound, and tries to guess the number using the method of divide and conquer.  It returns the number of guesses it makes.*)


let guess2 num (l: int) (u : int) = 
  let rec guessh l u num counter (gues : int) = 
    match gues with 
      |g when (g = num) -> (counter + 1)
      |g when (g > num) -> (guessh l g num (succ counter) ((g + l)/2))
      |g when (g < num) -> (guessh g u num (succ counter) ((g + u)/2))
  in guessh l u num 0 ((l + u)/2);;

(*test cases*)
(guess2 5 1 10) = 1;;
(guess2 2 1 10) = 3;;
(guess2 1 1 10) = 4;;

(*Procedure: my_sqrt*)

(*Type signature*)
(*my_sqrt: number -> number*)

(*Contract*)
(*my_sqrt calculates the approximate square root of an number, returning an exact number if its input is a perfect square, or the the smallest integer greater than/the largest integer less than the actual square root otherwise.*)

let my_sqrt num = 
  let rec myrt l u num gues = 
    match gues with
      |g when ((g*g) = num) -> g
      |g when (((g*g) < num) && (((succ g)*(succ g)) > num)) -> g
      |g when ((g*g) > num) -> (myrt l g num ((g + l)/2))
      |g when ((g*g) < num) -> (myrt g u num ((g + u)/2))
  in myrt 1 num num ((num + 1)/2);;

(*test cases*)
(my_sqrt 100) = 10;;
(my_sqrt 9) = 3;;
(my_sqrt 10) = 3;;
(my_sqrt 1029) = 32;;

(*Procedure: brute_force_power*)

(*Type signature:*)
(*brute_force_power: number, number -> number*)

(*Contract:*)
(*brute_force_power computes x^y for two natural numbers x and y*)

let rec brute_force_power x y =
  match y with
    |0 -> 1
    |n when (n > 0) -> x * (brute_force_power x (n - 1))
    |_ -> failwith "y is not a natural number";;

(*test cases*)
(brute_force_power 2 4) = 16;;
(brute_force_power 3 4) = 81;;
(brute_force_power 10 2) = 100;;

(*Procedure: super_power*)

(*Type signature:*)
(*super_power: number, number -> number*)

(*Contract*)
(*super_power takes as calculates x^y (using the divide-and-conquer method) for two natural numbers x and y*)

let rec super_power x y = 
  match y with 
    |n when (n = 0) -> 1
    |n when ((n mod 2) = 0) -> (super_power x (n/2))*(super_power x (n/2))
    |n when ((n mod 2) = 1) -> x*(super_power x (n/2))*(super_power x (n/2))
    |_ -> failwith "error";;

(*test cases*)
(super_power 1 10) = 1;;
(super_power 2 4) = 16;;
(super_power 3 12) = 531441;;


(*Helper Procedure: take*)

(*Type signature:*)
(*take: number, (listof data) -> (listof data)*)

(*Contract:*)
(*take takes as input a number, n, and a list of data, alod, and returns the first n elements of alod.*)


let rec take n alod = 
  match (n, alod) with
    |(0, _) -> []
    |(n, hd::tl) when (n>0) -> hd::(take (n - 1) tl)
    |_ -> failwith "Invalid input.";;

(*test cases*)
(take 3 [1;2;3]) = [1;2;3];;
(take 1 [1;2;3]) = [1];;
(take 0 [1;2;3]) = [];;

(*Helper Procedure: drop*)

(*Type signature:*)
(*drop: number, (listof data) -> (listof data)*)

(*Contract:*)
(*drop takes as input a number, n, and a list of data, alod, and returns all but the first n elements of alod.*)

let rec drop n alod =
  match (n, alod) with
    |(0, x) -> x
    |(n, hd::tl) when (n>0) -> (drop (n - 1) tl)
    |_ -> failwith "Invalid input.";;

(*test cases*)
(drop 3 [1;2;3]) = [];;
(drop 1 [1;2;3]) = [2;3];;
(drop 2 [1;3;4;5]) = [4;5];;


(*Helper Procedure: zeros*)

(*Type signature:*)
(*zeros: number -> (listof numbers)*)

(*Contract:*)
(*zeros takes as input a number, n, and returns a list of n zeros.  This is used to adjust places of digits in bignumt*)

let rec zeros n =
  match n with
    |0 -> []
    |n -> 0::(zeros (n-1));;

(*test cases*)
(zeros 1) = [0];;
(zeros 2) = [0;0];;
(zeros 3) = [0;0;0];;


(*Helper Procedure: fix*)

(*Type signature:*)
(*fix: bignum --> bignum*)

(*Contract:*)
(*Fix operates on lists of nonnegative numbers and converts them to "proper bignums" by carrying ones.  For example, given the list [10;10;10;10;0], fix returns [0;1;1;1;1].*)

let rec fix x =
  match x with
    |h::[] -> (if (h > 9) then ((h mod 10)::(h/10)::[]) else (h::[]))
    |h::t -> ((h mod 10)::(fix (((h/10) + (List.hd t))::(List.tl t))));;

(*test cases*)
(fix [10;10;10;10;0]) = [0;1;1;1;1];;
(fix [10]) = [0;1];;
(fix [9;9;9;10]) = [9;9;9;0;1];;


(*Helper Procedure: bignump*)

(*Type signature:*)
(*bignump: bignum, bignum -> bignum*)

(*Contract:*)
(*bignump (or bignumplus) takes as input two bignums and returns their sum.*)

let bignump x y =
  let rec bignumadd x y =
    match (x,y) with
      |([], []) -> []
      |(a, []) -> a
      |([], b) -> b
      |(h1::t1, h2::t2) -> ((h1+h2)::(bignumadd t1 t2))
  in (fix (bignumadd x y));;

(*test cases*)
(bignump [1;2;4;7;4;7;8;2] [2;3;5;7;1;3;4]) = [3;5;9;4;6;0;3;3];;
(bignump [1;2;3] [4;5;6]) = [5;7;9];;
(bignump  [0] [0]) = [0];;


(*Procedure: bignumt*)

(*Type signature*)
(*bignumt: bignum, bignum -> bignum *)

(*Contract:*)
(*bignumt (or bignum* ) computes xy given two BigNums x and y using divide-and-conquer approach.*)

let rec bignumt x y =
  let lenx = (List.length x) and leny = (List.length y) in 
  match (x,y) with
    |((0::[]), _) -> [0]
    |(_, (0::[])) -> [0]
    |((h1::[]), (h2::[])) -> (h1 * h2)::[]
    |(_, []) -> [0]
    |([] ,_) -> [0]
    |((h1::t1), (h2::t2)) -> (bignump ((bignump (bignumt (take (lenx/2) x) (take (leny/2) y))) ((zeros (leny/2))@(bignumt (take (lenx/2) x) (drop (leny/2) y)))) ((zeros (lenx/2))@(bignump (bignumt (drop (lenx/2) x) (take (leny/2) y)) ((zeros (leny/2))@(bignumt (drop (lenx/2) x) (drop (leny/2) y))))));;

(*test cases for this beast of a procedure.  I mean, jesus, would you look at the parentheses on that thing?*)
(bignumt [1;1;1;1] [2;3]) = [2; 5; 5; 5; 3];;
(bignumt [2;3;4;5] [1;2;3;3;4;5;6;7;8;2;2;3;5;7;8;9;2;1;2;3;5;8;8;8;8;8;8]) = [2; 7; 6; 9; 1; 3; 3; 8; 3; 5; 6; 8; 3; 5; 7; 2; 8; 4; 9; 2; 5; 6; 0; 5; 2;
                                                                               4; 4; 8; 2; 8; 4];; (*verified by wolframalpha*)
(bignumt [1;2;3;3;4;5;6;7;8;2;2;3;5;7;8;9;2;1;2;3;5;8;8;8;8;8;8] [0]) = [0];;
(bignumt [3;3;3;3;3;3] [1;1;1]) = [3;6;9;9;9;9;6;3];;
(bignumt  [0;0;0;1] [2]) = [0;0;0;2];;
(bignumt [2;2;3;1;3;1] [0;0;0;0;1;2;3;4;5]) = [0;0;0;0;2;6;3;2;4;5;3;3;1;7];;




    

