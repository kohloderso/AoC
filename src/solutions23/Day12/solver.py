class NonogramLine:
    """
    A class to represent a nonogram line.

    Parameters:
    hints (list of int): list of description hints, i.e. the list of contiguous groups
    of colored tiles.
    line (list of int): list of tiles in this line.
    Each tile is either colored black (1), or colored white (0), or unknown (-1)
    """

    def __init__(self, hints, line):
        self.hints = hints
        self.line = line
        self.sol = [[-1 for _ in range(len(hints) + 1)] for _ in range(len(line) + 1)]
        # initialize the first column of sol with 1
        for j in range(len(line) + 1):
            self.sol[j][0] = 1

    # Check whether jth block of tiles can be colored black ending at tile i without
    # contradicting any partial coloring that's already present.
    def can_place_block(self, i, j):
        b_size = self.hints[j]
        partial_line = self.line[i - b_size : i]
        # check if partial line contains a white tile (0)
        if 0 in partial_line or len(partial_line) < b_size:
            return False
        # if this is not the leftmost block (i.e. block 0) then we need at least one
        # white tile at the left, between block j and block j-1
        if j > 0 and self.line[i - b_size - 1] == 1:
            return False
        # if this is the leftmost block there can only be white tiles to the left
        if j == 0 and 1 in self.line[0 : i - b_size]:
            return False
        return True

    # compute the number of colourings of the first i tiles containing exactly
    # the first j blocks of black cells.
    def solve(self, i, j):
        if i < 0 or j < 0:
            return 0
        if self.sol[i][j] != -1:
            return self.sol[i][j]
        self.sol[i][j] = 0
        if self.solve(i - 1, j) > 0 and self.line[i - 1] != 1:
            self.sol[i][j] += self.solve(i - 1, j)
        b_size = self.hints[j - 1]
        need_white = 0 if j == 1 else 1
        if self.solve(i - b_size - need_white, j - 1) > 0 and self.can_place_block(
            i, j - 1
        ):
            self.sol[i][j] += self.solve(i - b_size - need_white, j - 1)
        return self.sol[i][j]

    def num_sols(self):
        return self.solve(len(self.line), len(self.hints))
