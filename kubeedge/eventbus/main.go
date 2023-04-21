package main

import (
	"github.com/gin-gonic/gin"
	"fmt"
	"io/ioutil"
)

func main() {
	r := gin.Default()
    r.POST("/bb", func(c *gin.Context) {
		s, _ := ioutil.ReadAll(c.Request.Body)
		fmt.Printf("edge say '%s' ", s)
	})
	r.Run(":6666")
}
