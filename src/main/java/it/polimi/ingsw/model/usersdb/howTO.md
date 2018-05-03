#come fare per far far qualcosa al player, dal controller.

COntroller:
1) ricevo un comando dal client, contenente (ad esempio) usatoolcard , cardid e il token.
2) faccio un player=getPlayerInGameFromToken(token)
3) faccio player.useToolCard(cardID)
se il player non è attivo mi arriva l'eccezione che non può fare niente