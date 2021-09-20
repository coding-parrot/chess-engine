# chess-engine

Part 1:

The chess engine can generate all legal moves for a given position upto an arbitrary depth.
This includes:
-- pawn promotions
-- enpassant moves
-- castling

The moves which are marked illegal are:
-- x-rays to the king
-- castling through check
-- moves of a piece pinned to the king
-- moving into check
-- enpassant after one move

The current move generator takes about 2 minutes to go to a depth of 6 ply. 


Part 2: (In progress) Game Engine

The game engine will use the move generator to select a move. The first version will be using a min-max tree with optimisations to find the best move.

This includes:
-- basic hueristic
-- iterative deepening
-- alpha-beta pruning
-- killer heuristic
-- quiescence search
-- null heuristic
-- parallel alpha-beta

Part 3: (Planning phase) Stochastic Chess Engine

The second version of the chess engine will use Monte Carlo Tree Search with basic hueristics. 

I do not hope much from it, since I don't have experience with neural network training and the compute power required to train NNs is usually high (I have a GPU, so not all hope lost). 
