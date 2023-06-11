package main

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"io/ioutil"
)

func main() {
	r := gin.Default()
	r.POST("/aa", func(c *gin.Context) {
		s, _ := ioutil.ReadAll(c.Request.Body)
		fmt.Printf("edge say '%s' ", s)
	})
	r.Run(":8080")
}