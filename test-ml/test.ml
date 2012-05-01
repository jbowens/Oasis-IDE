let rec func n =
        match n with
        |0 -> func n
        |_ -> 3;;

print_int(func(5));;
print_string("\n");;
print_int(func(0));;
