
(* myListLength
   Input: a list, aList
   Output: the number of items in aList *)
let rec myListLength( aList : 'a list) : int = match aList with
 | [] -> 0
 | _::tl -> 1 + myListLength tl;;


let list1 = [ 0; 1; 2; 3];;
(** #trace myListLength;; *)
let a = myListLength(list1);;
print_int(a);;
(** #untrace myListLength;; *)
print_string("\n");;
let list2 = [4; 5; 6; 7; 8];;
let b = myListLength(list2);;
print_int(b);;
print_string("\n");;
