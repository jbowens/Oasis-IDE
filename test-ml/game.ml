module type GAME =
sig
  
  (* TYPES *)
  
  type which_player = P1 | P2
  
  (* expresses the current state of the game, i.e., what
   * the board looks like and whose turn it is *)
  type state
  
  (* expresses a move for your particular game *)
  type move
  
  (* describes the status of the game, i.e. over or still in play *)
  type status
  
  (* INITIAL VALUES *)
  
  (* defines the state at the start of the game *)
  val initial_state : state
  
  (* GAME LOGIC *)
  
  (* determines whether move is legal in given state *)
  val is_legal_move : state -> move -> bool
  
  (* returns the list of moves that are allowed based on
   * the current state. *)
  val legal_moves : state -> move list
  
  (* returns the current player based on the state of the game *)
  val current_player : state -> which_player
  
  (* returns the game state after executing the input move. This function will
   * be responsible for switching players, board positions, and anything else
   * that changes during a move. *)
  val next_state : state -> move -> state
  
  (* Approximates the recursively-defined "value" of the game state.  Uses the
   * same sign for each player. That is, negative values are better for
   * P2 and positive values are better for P1. *)
  val estimate_value : state -> float
  
  (* returns the current status based on the current state *)
  val current_status : state -> status
  
  (* determines whether the game is over or not based on
   * the current status. *)
  val is_game_over : status -> bool
  
  
  (* TYPE CONVERSIONS *)
  
  (* returns the name of a player *)
  val string_of_player : which_player -> string
  
  (* describes the current state of your game. *)
  val string_of_state : state -> string
  
  (* describes a move, most likely with no mention of the
   * player. *)
  val string_of_move : move -> string
  
  (* describes the status of the game *)
  val string_of_status : status -> string
  
  (* useful for the human player. Takes a string and
   * returns the move that it represents. *)
  val move_of_string : string -> move

end ;;


(* Fill in the code for your game here. *)
module GameTest =
  struct
    let width = 7
    let height = 6
    type which_player = P1 | P2 
    type locationState = One | Two | Empty
    type state = State of (which_player * locationState list list)
    type status = InPlay | Draw | Over of which_player
    type move = Move of int
    
    (* build_matrix: int -> int -> locationState list list 
       Input: the number of rows, rows, and the number of columns, cols
       Output: a two-dimensional list (rows by cols) of instances of Elements *)
    let build_matrix (rows:int) (cols:int) = 
    	(* create_list : 'a int -> 'a list
    	   Input: a value and the number of times the value should appear in the list
    	   Output: a list of num instances of initial_value *)
    	let rec create_list initial_value (num:int) = match num with
    			| 0 -> []
    			| _ -> initial_value::(create_list initial_value (num-1)) in
    create_list (create_list Empty rows) cols
    		
    let initial_state = State(P1, build_matrix height width)
	
	(* is_legal_move: state move -> bool
	   Input: a game state and a potential move
	   Output: a bool representing whether the move is legal on a board of
	     	   the specified state. *)
    let is_legal_move state move = match (state,move) with
    | (State(_,board), Move(c)) -> if (c <= 0)
    then false
    else try (match (List.nth board (c-1)) with
    	| head::tail -> head = Empty
    	| [] -> false)	
    	with
    	| e -> false
  
  	(* legal_moves: state -> moves list 
  	   Input: a game state
  	   Output: a list of all legal moves given the inputted state. *)
    let legal_moves state = 
    	let rec find_moves (cols:locationState list list) (col:int) = match cols with
    	| [] -> []
    	| h::t -> if List.hd h = Empty 
    			  then Move(col)::find_moves t (col+1)
    			  else find_moves t (col+1) in
    match state with
    | State(_, board) -> find_moves board 1
      
    (* current_player: state -> player
       Input: a game state
       Output: the player whose current turn it is in the inputted state. *)
    let current_player state = 
      match state with 
        | State (player, _) -> player
    
    (* next_state : state -> move -> state
       Input: a state and a move to make
       Output: the resulting state after move is applied to state. *)
    let next_state state move = 
    	(* insert_piece_within_col : locationState list -> locationState -> locationState list
    	   Input: a column of locationStates, column, and a locationState to insert into the column, piece
    	   Output: the inputted column with piece inserted as far down the list as possible without overwriting
    	   			a One or Two *)
    	let rec insert_piece_within_col ( column : locationState list ) (piece : locationState ) = match column with 
    	| Empty::One::tail -> piece::One::tail
    	| Empty::Two::tail -> piece::Two::tail
    	| Empty::[] -> piece::[]
    	| head::tail -> head::insert_piece_within_col tail piece
    	| _ -> failwith "no room in column. illegal move." in
    		(* insert_piece : locationState list list -> int -> locationState -> locationState list list
    		   Input : a two-dimensional list representing the board, board, the column to insert the piece in, column
    		           and the piece to add to the column, piece.
    		   Output: board, except with piece added to the columnth column *)
    		let rec insert_piece (board : locationState list list) (column : int ) (piece : locationState) = match (board,column) with
    		| (head::tail,0) -> insert_piece_within_col head piece::tail
    		| (head::tail,c) -> head::insert_piece tail (c-1) piece
    		| ([],c) -> failwith "That column doesn't exist." in
    			match (state,move) with
    			| (State(p, board), Move(c)) -> State( (if p = P1 then P2 else P1), insert_piece board (c-1) (if p = P1 then One else Two ))
    			
    			    
    (* check_list : locationState list -> bool
       Input: a list of locationState values, l1
       Output: a boolean representing whether l1 contains 4 consecutive Ones or 
       		   4 consecutive Twos. *)
    let rec check_list (l1:locationState list) = match l1 with
    | [] -> false
    | One::One::One::One::_ -> true
    | Two::Two::Two::Two::_ -> true
    | h::t -> check_list t
    	
    (* check_lists : locationState list list -> bool
    	Input: a list of lists of locationStates
    	Output: true if all the inner lists do NOT have four Ones in a row
    	    	and do NOT have four Twos in a row. false otherwise. *)
    let rec check_lists (lol: locationState list list) = match lol with
    | [] -> false
    | head::tail -> if check_list head then true else (check_lists tail)
    		
    (* extract_rows : locationState list list -> int -> locationState list list
    	   Input: a two-dimensional list representing the columns and their values, columns
    	          and a int representing the current row that is being extracted.
    	   Output: columns but restructed so rows are in the outside list instead of columns. *)
    let rec extract_rows (columns: locationState list list ) (row:int) = match row with
    	| 0 -> []
    	| _ -> (List.map (fun (col) -> List.nth col (row-1)) columns)::extract_rows columns (row-1)
    		
    (* create_right_diag : locationState list list -> int -> int -> locationState list
       Input: a two-dimension list of columns and their values, board
       		  an integer representing the current column, x
       		  an integer representing the current row, y
       Output: The right diagonal that begins at (x,y) *)
    let rec create_right_diag (board: locationState list list) (x:int) (y:int) =
    if x = 0 or y = (height-1) then (List.nth (List.nth board x) y)::[] else (List.nth (List.nth board x) y)::(create_right_diag board (x-1) (y+1))
    		
    (* create_left_diag : locationState list list -> int -> int -> locationState list
       Input: a two-dimension list of columns and their values, board
       		  an integer representing the current column, x
       		  an integer representing the current row, y
       Output: The left diagonal that begins at (x,y) *)   
    let rec create_left_diag (board: locationState list list) (x:int) (y:int) =
    if x = (width-1) or y = (height-1) then (List.nth (List.nth board x) y)::[] else (List.nth (List.nth board x) y)::create_left_diag board (x+1) (y+1)
    		
    (* get_right_diags : locationState list list -> int -> int -> locationState list list
       Input: a two-dimension list of columns and their values, board
       		  an integer representing the current column, x
       		  an integer representing the current row, y
       Output: All of the right diagonals on the board *)
    let rec get_right_diags (board: locationState list list) (startX:int) (startY:int) =
    if startY = height-1
    then [] 
    else create_right_diag board startX startY::(if startX = (width-1)
    											then get_right_diags board startX (startY+1)
    											else get_right_diags board (startX+1) startY)
    											
    (* get_left_diags : locationState list list -> int -> int -> locationState list list
       Input: a two-dimension list of columns and their values, board
       		  an integer representing the current column, x
       		  an integer representing the current row, y
       Output: All of the left diagonals on the board *)	
   	let rec get_left_diags (board: locationState list list) (startX:int) (startY:int) =
    if startY = height-1
    then []
    else create_left_diag board startX startY::(if startX = 0
    											then get_left_diags board 0 (startY+1)
    											else get_left_diags board (startX-1) startY)
    		

         
    (* estimate_value : state -> float
    	Input: a state to estimate thOne::One::Onee value of for the current player
    	Output: a float representing an estimation of the value of the state for the
    			current player. The larger the number, the more favorable the state is *)
    let estimate_value state = match state with
      | State (player, board) -> let calculate_value_subsection (l1 : locationState list) = (match l1 with
          | [] -> 0.0
          | One::One::One::One::_ -> 10000000.0
          | Two::Two::Two::Two::_ -> -10000000.0
          | Empty::One::One::One::Empty::_ -> 230.0
          | Empty::Two::Two::Two::Empty::_ -> -230.0
          | One::One::One::Empty::_ -> 80.0
          | Two::Two::Two::Empty::_ -> -80.0
          | Empty::One::One::One::_ -> 80.0
          | Empty::Two::Two::Two::_ -> -80.0
          | One::Empty::One::One::_ -> 80.0
          | Two::Empty::Two::Two::_ -> -80.0
          | One::One::Empty::One::_ -> 80.0
          | Two::Two::Empty::Two::_ -> -80.0
          | Empty::Empty::One::One::Empty::_ -> 10.0
          | Empty::Empty::Two::Two::Empty::_ -> -10.0
          | Empty::One::One::Empty::Empty::_ -> 35.0
          | Empty::Two::Two::Empty::Empty::_ -> -35.0
          | Empty::One::One::Empty::_ -> 25.0
          | Empty::Two::Two::Empty::_ -> -25.0
          | One::One::Empty::_ -> 3.0
          | Two::Two::Empty::_ -> -3.0
          | Empty::One::One::_ -> 3.0
          | Empty::Two::Two::_ -> -3.0
          | Empty::One::Empty::_ -> 0.25
          | Empty::Two::Empty::_ -> -0.25
          | _ -> 0.0)
    in
        let rec calculate_value_list (l1 : locationState list) = match l1 with
        | [] -> 0.0
        | head::tail -> calculate_value_subsection l1 +. calculate_value_list tail
    	in
    	let rec calculate_value_lists (lists : locationState list list) = match lists with
    	| [] -> 0.0
    	| head::tail -> calculate_value_list head +. calculate_value_lists tail 
    	in 
    	   calculate_value_lists board +.
    	   calculate_value_lists (extract_rows board height) +. 
    	   calculate_value_lists (get_right_diags board 0 0) +.
    	   calculate_value_lists (get_left_diags board (width-1) 0)
    
    (* current_status : state -> status 
       Input: a state
       Output: a status description of the inputted state. *)
    let current_status state = 
    	match state with
    	| State(p,board) -> if check_lists board
    						or check_lists (extract_rows board height) 
    						or check_lists (get_right_diags board 0 0)
    						or check_lists (get_left_diags board (width-1) 0)
    						then Over(if p = P1 then P2 else P1)
          else InPlay
    
    (* is_game_over : status -> bool
       Input: a status description of a game
       Output: a boolean representing whether the game has ended, based on the inputted status *)		    
    let is_game_over status = match status with
      | InPlay -> false
      | _ -> true
    
    (* string_of_player : player -> string
       Input: a player
       Output: the string representation of the inputed player *)
    let string_of_player x = 
      match x with
        | P1 -> "Player 1"
        | P2 -> "Player 2"
        
    (* string_of_state : state -> string
       Input: a game state
       Output: a string reprenstation of the game state, including
       		   whose turn it is and the current state of the board. *)
    let string_of_state state = match state with
    | State(player, board) -> "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
    ^ "-------------------------------------------------------------------\nConnect Four - " ^ string_of_player( player ) ^ "'s turn\n\n" ^
    	let rec columnNumbers (col : int) = if col > width then "\n" else string_of_int col ^ "\t" ^ columnNumbers (col+1) in
    "\t" ^ columnNumbers 1 ^ "\n\n" ^
    
    	let rec printRows (row: int) = if row = height then "\n" else (List.fold_left (fun final p -> final ^ (match (List.nth p row) with
    																					| Empty -> " "
    																					| One -> "X"
    																					| Two -> "O") ^ "\t") "" board) ^ "\n\n\n\t" ^ printRows (row+1) in
    "\t" ^ printRows 0 ^
    "-------------------------------------------------------------------\n"
      
    (* string_of_move: move -> string
       Input: a move
       Output: a string representation of the inputted move *)
    let string_of_move x = 
      match x with
        | Move (x) -> "Column " ^ (string_of_int x)
    
    (* string_of_status: status -> string
       Input: a status
       Output: a string representation of the status *)
    let string_of_status status = match status with
    | Draw -> "Draw"
    | Over(p) -> "Game Over, " ^ (string_of_player p) ^ " won."
    | InPlay -> "In play"
    
    (* move_of_string :  string -> move
       Input: a string representation of a move
       Output: a Move that represents the inputted string.*)
    let move_of_string x = Move (int_of_string x)

        
  end
  

  

  
module Game = ( GameTest : GAME );;
  
module type GAME_PLAYER =
sig
  (* takes a state from the game, and decides which legal move to make. *)
  val next_move : Game.state -> Game.move
end

  
(* The HumanPlayer just gets moves from what the user types in. To define this
 * next_move proc, it has to use the GAME to find out whether a move is legal
 * and whose turn it is.  We've written this one for you because it uses
 * input/output code, which isn't purely functional.
 *
 * Note that your AI player simply needs to find the next best move for it. It
 * does not have to worry about printing to the screen or some of the other
 * silliness in this chunk of code. *)
module HumanPlayer : GAME_PLAYER =
  struct
    type state = Game.state
    type move = Game.move
    
    let move_string state =
      (Game.string_of_player (Game.current_player state)) ^
        ", please type your move: "
    
    let bad_move_string str =
      "Bad move for this state: " ^ str ^ "!\n"
    
    let rec next_move state =
      print_string (move_string state);
      flush stdout;
      try
        let str = (input_line stdin)
        in let new_move = Game.move_of_string str
        in if (Game.is_legal_move state new_move)
           then new_move
           else (print_string (bad_move_string str); next_move state)
      with _ -> (print_endline "That move makes no sense!"; next_move state)
end

(* Fill in the code for your AI player here. *)
module AIPlayer : GAME_PLAYER =
  struct
    type state = Game.state
    type move = Game.move
    type status = Game.status
    let minimax_depth = 1
    
    (* next_move : state -> move
       Input: the current state of the game, state1
       Ouput: a move
       This function returns the move that the AIPlayer chooses to make*)
    let next_move state = 
      let rec maxmin (state1 : Game.state ) (depth : int) = (match depth with
        | 0 -> Game.estimate_value state1
        | _ -> if Game.is_game_over (Game.current_status (state1 : Game.state))
        	   then (if Game.current_player state1 = Game.P1 then -10000000.0 else 10000000.0)
        	   else let legal_moves = Game.legal_moves state1 in 
      let rec get_best_value ( moves: Game.move list ) ( best_value : float ) = match moves with
        | [] -> best_value
        | head::tail -> let current_value = (maxmin (Game.next_state state1 head) (depth-1)) in
          if (if (Game.current_player state1) = Game.P1
              then current_value > best_value
              else current_value < best_value)
          then get_best_value tail current_value
          else get_best_value tail best_value
      in
      	match legal_moves with
      	| head::tail -> get_best_value tail (maxmin (Game.next_state state1 head) (depth-1))
      	| _ -> 0.0)
      
      in
    
        let legal_moves = Game.legal_moves state in
    	let rec get_best_move (moves : Game.move list) best_move best_value = match moves with
          | [] -> best_move
          | head::tail -> let value = maxmin (Game.next_state state head) minimax_depth in
            if (if Game.current_player state = Game.P1
                then value > best_value
                else value < best_value) 
            then get_best_move tail head value 
            else get_best_move tail best_move best_value
        in
          get_best_move (List.tl legal_moves) (List.hd legal_moves) (maxmin (Game.next_state state (List.hd legal_moves)) minimax_depth)
  end
  

  
(* The Referee coordinates the other modules. Call
 * start_game to play a game. *)
module Referee =
  struct
    
    module Player1 = HumanPlayer
    module Player2 = AIPlayer
    
    let game_on_string player move =
      (Game.string_of_player player) ^
        " decides to make the move " ^
        (Game.string_of_move move) ^ "."
    
    let rec play state =
      print_endline (Game.string_of_state state);
      if (Game.is_game_over (Game.current_status state))
      then (print_endline ("Game over! Final Status: " ^ (Game.string_of_status (Game.current_status state))); ())
      else
    let player = Game.current_player state in
    let move = match player with
      | Game.P1 -> Player1.next_move state
      | Game.P2 -> Player2.next_move state in
        print_endline (game_on_string player move);
        play (Game.next_state state move)
    
    let start_game () = play (Game.initial_state)

end;;

(* Test Cases *)

GameTest.build_matrix 3 3 = [[GameTest.Empty;GameTest.Empty;GameTest.Empty]; [GameTest.Empty;GameTest.Empty;GameTest.Empty]; [GameTest.Empty;GameTest.Empty;GameTest.Empty]];;
GameTest.build_matrix 2 2 = [[GameTest.Empty;GameTest.Empty]; [GameTest.Empty;GameTest.Empty]];;
GameTest.build_matrix 1 1 = [[GameTest.Empty]];;

GameTest.is_legal_move GameTest.initial_state (GameTest.Move (1000)) = false;;
GameTest.is_legal_move GameTest.initial_state (GameTest.Move (0)) = false;;
GameTest.is_legal_move GameTest.initial_state (GameTest.Move (1)) = true;;

GameTest.string_of_move (GameTest.Move(5)) = "Column 5";;
GameTest.string_of_move (GameTest.Move(10)) = "Column 10";;
GameTest.string_of_move (GameTest.Move(11)) = "Column 11";;

GameTest.legal_moves GameTest.initial_state = [(GameTest.Move (1)); (GameTest.Move (2)); (GameTest.Move (3)); (GameTest.Move (4)); (GameTest.Move (5)); (GameTest.Move (6)); (GameTest.Move (7))];;
GameTest.legal_moves (GameTest.State (GameTest.P1, GameTest.build_matrix 2 2)) = [GameTest.Move (1); GameTest.Move (2)];;
GameTest.legal_moves (GameTest.State (GameTest.P1, GameTest.build_matrix 3 3)) = [GameTest.Move (1); GameTest.Move (2); GameTest.Move (3)];;

GameTest.current_player GameTest.initial_state = GameTest.P1;;
GameTest.current_player (GameTest.State (GameTest.P1, GameTest.build_matrix 3 3)) = GameTest.P1;;
GameTest.current_player (GameTest.State (GameTest.P2, GameTest.build_matrix 7 7)) = GameTest.P2;;

GameTest.next_state (GameTest.State (GameTest.P1, (GameTest.build_matrix 2 2))) (GameTest.Move (1)) = (GameTest.State (GameTest.P2, [[GameTest.Empty; GameTest.One]; [GameTest.Empty; GameTest.Empty]]));;
GameTest.next_state (GameTest.State (GameTest.P1, GameTest.build_matrix 2 2)) (GameTest.Move (2)) = GameTest.State (GameTest.P2, [[GameTest.Empty; GameTest.Empty]; [GameTest.Empty; GameTest.One]]);;
GameTest.next_state (GameTest.State (GameTest.P2, GameTest.build_matrix 3 3)) (GameTest.Move (2)) = GameTest.State (GameTest.P1, [[GameTest.Empty; GameTest.Empty; GameTest.Empty]; [GameTest.Empty; GameTest.Empty; GameTest.Two]; [GameTest.Empty; GameTest.Empty; GameTest.Empty]]);;

GameTest.check_list [GameTest.One; GameTest.One; GameTest.One; GameTest.One] = true;;
GameTest.check_list [GameTest.Two; GameTest.One; GameTest.One; GameTest.One; GameTest.One] = true;;
GameTest.check_list [GameTest.One; GameTest.One; GameTest.One] = false;;
GameTest.check_list [GameTest.Two; GameTest.Two; GameTest.Two; GameTest.Two] = true;;

GameTest.check_lists [[GameTest.One; GameTest.One]; [GameTest.One; GameTest.One]] = false;;
GameTest.check_lists [[GameTest.One; GameTest.One; GameTest.One; GameTest.One]; [GameTest.Two; GameTest.Empty]] = true;;
GameTest.check_lists [[GameTest.Empty]; [GameTest.Empty]; [GameTest.Empty]] = false;;

GameTest.extract_rows (GameTest.build_matrix 2 2) 2 = (GameTest.build_matrix 2 2);;
GameTest.extract_rows (GameTest.build_matrix 2 2) 1 = (GameTest.build_matrix 2 1);;
GameTest.extract_rows (GameTest.build_matrix 5 5) 3 = (GameTest.build_matrix 5 3);;

(* create_right_diag only works with matricies of the dimensions specified by GameTest.height and GameTest.width *)
GameTest.create_right_diag (GameTest.build_matrix 5 5) 0 0 = [GameTest.Empty];;
GameTest.create_right_diag (GameTest.build_matrix 5 5) 4 0 = [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];;
GameTest.create_right_diag (GameTest.build_matrix 3 3) 1 1 = [GameTest.Empty; GameTest.Empty];;

(* get_right_diags only works with matricies of the dimensions specified by GameTest.height and GameTest.width *)
GameTest.get_right_diags (GameTest.build_matrix GameTest.height GameTest.width) 0 0 = 
[[GameTest.Empty]; [GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty]];;
 GameTest.get_right_diags (GameTest.build_matrix GameTest.height GameTest.width) 0 2 = [[GameTest.Empty]; [GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty]];;
 
(* create_left_diag only works with matricies of the dimensions specified by GameTest.height and GameTest.width *)
GameTest.create_left_diag (GameTest.build_matrix GameTest.height GameTest.width) 0 0 = [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
 GameTest.Empty; GameTest.Empty];;
GameTest.create_left_diag (GameTest.build_matrix GameTest.height GameTest.width) 6 5 = [GameTest.Empty];;
GameTest.create_left_diag (GameTest.build_matrix GameTest.height GameTest.width) 6 4 = [GameTest.Empty];;
 
(* get_left_diags only works with matricies of the dimensions specified by GameTest.height and GameTest.width *)
GameTest.get_left_diags (GameTest.build_matrix GameTest.height GameTest.width) (GameTest.width-1) 0 = [[GameTest.Empty]; [GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty]];;
GameTest.get_left_diags (GameTest.build_matrix GameTest.height GameTest.width) 0 0 = [[GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
  GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty; GameTest.Empty];
 [GameTest.Empty; GameTest.Empty]];;
GameTest.get_left_diags (GameTest.build_matrix GameTest.height GameTest.width) 0 (GameTest.height-1) = [];;

GameTest.estimate_value GameTest.initial_state = 0.0;;
GameTest.estimate_value (GameTest.State
 (GameTest.P1,
  [[GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Two; GameTest.One];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty]])) = -0.5;;
GameTest.estimate_value (GameTest.State
 (GameTest.P1,
  [[GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Two];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.One];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty]])) = 0.0;;
    
GameTest.current_status (GameTest.State(GameTest.P1,
  [[GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Two; GameTest.One];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty]])) = GameTest.InPlay;;
GameTest.current_status (GameTest.State
 (GameTest.P1,
  [[GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Two];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.One];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty]])) = GameTest.InPlay;;
GameTest.current_status (GameTest.State
 (GameTest.P2,
  [[GameTest.Empty; GameTest.Empty; GameTest.One; GameTest.One; GameTest.One;
    GameTest.One];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Two;
    GameTest.Two; GameTest.Two];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty]])) = GameTest.Over(GameTest.P1);;
    
GameTest.is_game_over (GameTest.Over(GameTest.P1)) = true;;
GameTest.is_game_over GameTest.InPlay = false;;
GameTest.is_game_over GameTest.Draw = true;;

GameTest.string_of_player GameTest.P1 = "Player 1";;
GameTest.string_of_player GameTest.P2 = "Player 2";;

GameTest.string_of_state GameTest.initial_state = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n-------------------------------------------------------------------\nConnect Four - Player 1's turn\n\n\t1\t2\t3\t4\t5\t6\t7\t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t\n-------------------------------------------------------------------\n";;
GameTest.string_of_state (GameTest.State
 (GameTest.P2,
  [[GameTest.Empty; GameTest.Empty; GameTest.One; GameTest.One; GameTest.One;
    GameTest.One];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Two;
    GameTest.Two; GameTest.Two];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty]])) = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n-------------------------------------------------------------------\nConnect Four - Player 2's turn\n\n\t1\t2\t3\t4\t5\t6\t7\t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\tX\t \t \t \t \t \t \t\n\n\n\tX\tO\t \t \t \t \t \t\n\n\n\tX\tO\t \t \t \t \t \t\n\n\n\tX\tO\t \t \t \t \t \t\n\n\n\t\n-------------------------------------------------------------------\n";;
GameTest.string_of_state (GameTest.State
 (GameTest.P1,
  [[GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Two];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.One];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty];
   [GameTest.Empty; GameTest.Empty; GameTest.Empty; GameTest.Empty;
    GameTest.Empty; GameTest.Empty]])) = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n-------------------------------------------------------------------\nConnect Four - Player 1's turn\n\n\t1\t2\t3\t4\t5\t6\t7\t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \t \t \t \t \t \t\n\n\n\t \t \tO\t \tX\t \t \t\n\n\n\t\n-------------------------------------------------------------------\n";;
    
GameTest.string_of_move (GameTest.Move(1)) = "Column 1";;
GameTest.string_of_move (GameTest.Move(3)) = "Column 3";;
GameTest.string_of_move (GameTest.Move(6)) = "Column 6";;

GameTest.string_of_status GameTest.InPlay = "In play";;
GameTest.string_of_status GameTest.Draw = "Draw";;
GameTest.string_of_status (GameTest.Over(GameTest.P2)) = "Game Over, Player 2 won.";;

GameTest.move_of_string "5" = GameTest.Move(5);;
GameTest.move_of_string "1" = GameTest.Move(1);;
GameTest.move_of_string "6" = GameTest.Move(6);;

Referee.start_game ();;
    
