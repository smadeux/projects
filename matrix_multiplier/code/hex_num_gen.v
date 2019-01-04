`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Create Date: 10/27/2018 10:15:43 AM
// Design Name: 
// Module Name: hex_num_gen
// Project Name: Matrix Multiplier 
// Description: Assign number that will be output on the seven segment display
//              based on which digit is currently being displayed
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module hex_num_gen(
    input [3:0] digit_sel,
    input [15:0] sw,
    output reg [3:0] hex_num
    );
    
    //Assign hex_num to the proper bits based on the digit being displayed
    always@(*)
    begin
    case (digit_sel)
        4'b1110 : hex_num <= sw[3:0];
        4'b1101 : hex_num <= sw[7:4];
        4'b1011 : hex_num <= sw[11:8];
        4'b0111 : hex_num <= sw[15:12];
        default : hex_num <= sw[3:0];
    endcase
    end
endmodule
