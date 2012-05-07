
(* myListLength
   Input: a list, aList
   Output: the number of items in aList *)
let rec myListLength( aList : 'a list) : int = match aList with
 | [] -> 0
 | _::tl -> 1 + myListLength tl;;
