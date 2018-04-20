# 4 threads, 10 calculations

### Task
Find the most effective way to distribute 10 calculations across no more than 4 threads at one time.

### Solution
Atomic integer as next calculation in line + threads grabbing tasks in `while` loop.
