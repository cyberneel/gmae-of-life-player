# What is it?
This mod is basically a port of the famous Conway's Game of Life. The game is simple, you edit state of the grid and simulate the next step to see how it evolves based on pre-set rules.
## The Rules
1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
2. Any live cell with two or three live neighbours lives on to the next generation.
3. Any live cell with more than three live neighbours dies, as if by overpopulation.
4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

# How to use?
You can create a 10x10 grid by running the command:
```
/spawn_gol_grid
```
This will create a gray grid nearby for you to interact with. Breaking a block removes the entire grid, while "using" or right-clicking on a block toggles its state between Alive (Red) and Dead (Gray).

Once you are ready and want to see how your grid evolves, run the following command:
```
/simulate_gol_step
```
This will simulate the grid by one step, you can keep running this command to see more evolution steps, or modify the grid before another simulation step.

# What's Next?
I plan on adding more features and making the experience better as I learn more about MC modding. Some things I have planned are:
- Simulate multiple steps at once
- Better colors and textures
- Reverse simulation
- A control block for settings

