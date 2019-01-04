`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Create Date: 11/29/2018 11:31:19 AM
// Module Name: bin2BCD_4dig
// Project Name: Matrix Multiplier
// Description: Module to control all four BCD digits and how they are displayed
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module bin2BCD_4dig(
    input clk,
    input [15:0] sw,
    output [6:0] seg,
    output [3:0] an,
    output dp
    );
    
    reg [3:0] bit_cnt;
    wire [3:0] dout;
    wire [3:0] BCD3, BCD2, BCD1, BCD0;
    reg [3:0] out3, out2, out1, out0;
    reg din;
    reg done;
    wire overflow;
    reg btnC;
    
    bin2BCD_1dig dig0 (.done(done), .d_in(din), .clk(clk), .d_out(dout[0]), .Q(BCD0));
    bin2BCD_1dig dig1 (.done(done), .d_in(dout[0]), .clk(clk), .d_out(dout[1]), .Q(BCD1));
    bin2BCD_1dig dig2 (.done(done), .d_in(dout[1]), .clk(clk), .d_out(dout[2]), .Q(BCD2));
    bin2BCD_1dig dig3 (.done(done), .d_in(dout[2]), .clk(clk), .d_out(dout[3]), .Q(BCD3));
    sseg_x4_top disp0 (.sw({out3,out2,out1,out0}), .seg(seg), .an(an), .dp(dp), .clk(clk), .btnC(1'b0));
    // An optional input to the 7-segment display may be added for an input to the decimal point to handle the overflow

    assign overflow = !(sw > 16'h270F);
    
    //count down from 16
    initial bit_cnt = 4'hF;
    always @ (posedge clk)
    begin
      bit_cnt = bit_cnt - 1;
    end
    
    //out0-3 will be sent to the seven segment display
    initial {out3, out2, out1, out0} = 16'h0000;
    always @ (posedge clk)
    begin 
      if (done)
         {out3, out2, out1, out0} <= {BCD3[2:0], BCD2[3:0], BCD1[3:0], BCD0[3:0], din};
    end
   
    //Set the next bit to be used as input
    //Set if done or not based on count
    always @ (bit_cnt, sw)
    begin
      din = sw[bit_cnt];
      done = (bit_cnt == 4'b0000);
    end
    
endmodule
