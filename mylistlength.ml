let rec listlength(alist : 'a list) = match alist with
| [] -> 0
| _::tl -> 1 + listlength tl;;

listlength [5; 4; 3; 2; 1] = 5;;
listlength [] = 0;;
listlength [4; 3] = 2;;
listlength [4; 3; 2; 1; 2; 3; 4] = 7;;
listlength [3] = 1;;
