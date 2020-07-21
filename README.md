# Maze-Search-Algorithms
Simple program implementing DFS and A* search algoirthms.

Depth-first search (DFS) is a search algorithm that starts at the root node and explores as far as possible along each branch before backtracking until a neighbouring node can be traversed
A-Star(A*) is a search algorithm that plots a walkable path between multiple nodes, or points, on the graph. The algorithm uses a heuristic value to plan ahead at each step so a more optimal decision is made.

A* Search 
`_  _  _  _  _  R    _  _  _  _  _  R  
_  _  _  v  _  R    _  _  _  v  _  R  
_  _  R  R  _  _    _  _  R  R  *  _  
R  _  _  _  _  _    R  _  *  *  _  _  
_  _  _  _  _  R    _  *  _  _  _  R  
*  _  R  _  _  R    *  _  R  _  _  R ` 

DFS Search 
_  _  _  _  _  R    _  *  *  *  *  R  
_  _  _  v  _  R    _  *  _  v  *  R  
_  _  R  R  _  _    _  *  R  R  *  *  
R  _  _  _  _  _    R  *  _  _  *  *  
_  _  _  _  _  R    *  _  _  _  *  R  
*  _  R  _  _  R    *  _  R  _  _  R  

## Usage:
`lein repl
(dfs "filename" <true|false>)
(astar "easy" <true|false>)`

The input file is a CSV maze grid, a grid point can be either a `_: Blank Point R:Impassable Wall *:Starting Point v:End Goal`
