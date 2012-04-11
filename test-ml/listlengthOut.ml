
(* myListLength
   Input: a list, aList
   Output: the number of items in aList *)
let rec myListLength( aList : 'a list) : int = match aList with
 | [] -> 0
 | _::tl -> 1 + myListLength tl;;


let list1 = [ 0; 1; 2; 3];;
let a = myListLength(list1);;
print_int(a);;
print_string("\n");;
