from pathlib import Path

from solver import NonogramLine


def parse_line(line):
    # split line at whitespace, yields pre-filled row
    # as first part and hints as second part
    split = line.strip().split()
    # convert the row into format used by the solver
    # '?' -> -1, '.' -> 0, '#' -> 1
    row = [{".": 0, "?": -1, "#": 1}.get(c) for c in split[0]]
    # split second part into individual hints
    hints = [int(hint) for hint in split[1].split(",")]
    return hints, row


def unfold_line(hints, row, n):
    # the hints are simply repeated n times
    hints_times_n = []
    for _ in range(n):
        hints_times_n.extend(hints)
    # the row is copied n times, but a -1 should be placed in between each copy
    row_times_n = []
    for _ in range(n):
        row_times_n.extend(row)
        row_times_n.append(-1)
    row_times_n.pop()
    return hints_times_n, row_times_n


def part1(input12):
    # for each line parse the line and then feed it to the solver
    # sum them all up
    res = 0
    for line in input12:
        hints, row = parse_line(line)
        res += NonogramLine(hints, row).num_sols()
    return res


def part2(input12):
    # for each line parse the line, unfold it 5 times, and then feed it to the solver
    # sum them all up
    res = 0
    for line in input12:
        hints, row = parse_line(line)
        hints, row = unfold_line(hints, row, 5)
        res += NonogramLine(hints, row).num_sols()
    return res


ROOT_DIR = Path(__file__).parent.parent

file = ROOT_DIR / "input" / "Day12.txt"
with file.open() as fin:
    input12 = fin.readlines()
    print(part1(input12))
    print(part2(input12))
