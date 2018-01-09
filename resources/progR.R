set.seed(1)

library(rpart)

# Données
data <- read.csv("resources/iris.csv")

# Taille de l'échantillon
n<-nrow(data) 
# Nombre de blocs
K<-10

# Échantillons
test_rows = sample.int(nrow(data), nrow(data)/5)
test = data[test_rows,]
train = data[-test_rows,]
ntrain<-nrow(train)

# Tirer une permutation
alea=runif(ntrain)
rang=rank(alea)
rang[1:10]

# Taille de chaque échantillon
taille<-ntrain%/%K
taille

# Compo des blocs

#KFold
block=(rang-1)%/%taille+1
table(block)


err.cv=numeric(0)
for(k in 1:K){
  CART=rpart(Species~.,train[block!=k,], method="class",
             parms=list( split='gini'))
  predCART=predict(CART,train[block==k,],type="class")
  matCART=table(train$Species[block==k],predCART)
  taux_err_CART= sum(predCART!= train$Species[block==k])/nrow(train[block==k,])
  err.cv=rbind(err.cv,taux_err_CART)
}
err.cv 
err_final <- mean(err.cv)

#apprentissage
modCART=rpart(Species~.,train, method="class",parms=list( split='gini'))
#test
modpredCART=predict(modCART,test,type="class")

#matrice de confusion
modmatCART=table(test$Species,modpredCART)

#taux d'erreur
modtaux_err_CART= sum(modpredCART!= test$Species)/nrow(test)
  
print(modtaux_err_CART)
